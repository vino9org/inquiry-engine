package net.vino9.vinobank.inqeng.api.dgs;

import graphql.relay.*;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public interface ConnectionAssembler {
    // works only for forward pagination, not backward pagination
    static <T> Connection<T> convert(List<T> listOfEdges, int pageNumber, int totalPages) {
        AtomicInteger index = new AtomicInteger();
        List<Edge<T>> defaultEdges = Stream.ofNullable(listOfEdges)
                .flatMap(Collection::stream)
                .map(e -> (Edge<T>) new DefaultEdge<>(e, new DefaultConnectionCursor(String.valueOf(index.incrementAndGet()))))
                .toList();
        var startCursor = String.valueOf(pageNumber);
        var endCursor = String.valueOf(pageNumber < totalPages - 1 ? pageNumber + 1 : pageNumber);
        return new DefaultConnection<>(defaultEdges,
                new DefaultPageInfo(new DefaultConnectionCursor(startCursor),
                        new DefaultConnectionCursor(endCursor),
                        pageNumber > 0,
                        pageNumber < totalPages - 1));

    }
}
