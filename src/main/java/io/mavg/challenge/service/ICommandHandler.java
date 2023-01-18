package io.mavg.challenge.service;

public interface ICommandHandler<T extends ICommand, R extends ICommandResponse> {

	R handle(T command);
}
