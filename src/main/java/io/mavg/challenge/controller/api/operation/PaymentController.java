package io.mavg.challenge.controller.api.operation;

import io.mavg.challenge.controller.api.account.model.AccountDto;
import io.mavg.challenge.controller.api.operation.model.PaymentRequest;
import io.mavg.challenge.controller.api.operation.model.PaymentResponse;
import io.mavg.challenge.service.ICommandHandler;
import io.mavg.challenge.service.payment.command.ProcessPaymentOperationCommand;
import io.mavg.challenge.service.payment.command.ProcessPaymentOperationCommandResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);

	private final ICommandHandler<ProcessPaymentOperationCommand, ProcessPaymentOperationCommandResponse> processPaymentCommand;

	public PaymentController(ICommandHandler<ProcessPaymentOperationCommand, ProcessPaymentOperationCommandResponse> processPaymentCommand) {
		this.processPaymentCommand = processPaymentCommand;
	}

	@PostMapping
	public PaymentResponse payment(
			Principal principal,
			@Valid @RequestBody PaymentRequest paymentRequest
	) {
		LOGGER.info("Payment process, user: {}", principal.getName());

		var paymentProcessResponse = processPaymentCommand.handle(
				new ProcessPaymentOperationCommand(
						paymentRequest.accountNumberOrigin(),
						paymentRequest.accountNumberDestination(),
						paymentRequest.amount(),
						principal.getName()
				)
		);

		return new PaymentResponse(
				paymentProcessResponse.transactionId(),
				new AccountDto(
						paymentProcessResponse.accountOrigin().accountHolderId(),
						paymentProcessResponse.accountOrigin().accountNumber(),
						paymentProcessResponse.accountOrigin().balance()
				),
				new AccountDto(
						paymentProcessResponse.accountDestination().accountHolderId(),
						paymentProcessResponse.accountDestination().accountNumber(),
						paymentProcessResponse.accountDestination().balance()
				),
				paymentProcessResponse.status(),
				paymentProcessResponse.errorCode()
		);
	}

}
