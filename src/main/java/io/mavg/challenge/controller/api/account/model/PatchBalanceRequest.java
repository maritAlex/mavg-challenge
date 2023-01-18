package io.mavg.challenge.controller.api.account.model;

import java.math.BigDecimal;

public record PatchBalanceRequest(
		BigDecimal amount
) {
}
