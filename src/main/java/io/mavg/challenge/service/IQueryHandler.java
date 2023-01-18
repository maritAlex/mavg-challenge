package io.mavg.challenge.service;

public interface IQueryHandler<Q extends IQuery, R extends IQueryResponse> {

	R query(Q query);
}
