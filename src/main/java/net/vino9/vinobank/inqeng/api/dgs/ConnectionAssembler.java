package net.vino9.vinobank.inqeng.api.dgs;

import graphql.relay.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConnectionAssembler {
    // works only for forward pagination, not backward pagination
    public static <T, S> Connection<T> fromPageable(Page<S> pageOfDataModels, ModelMapper mapper) {
        var type = (new TypeToken<List<S>>() {
        }).getType();
        List<S> listOfEdges = mapper.map(pageOfDataModels.getContent(), type);

        // the logic of generating unique cursor id should be replaced with a better one
        AtomicInteger index = new AtomicInteger();
        List<Edge<T>> defaultEdges = Stream.ofNullable(listOfEdges)
                .flatMap(Collection::stream)
                .map(e -> (Edge<T>) new DefaultEdge<>(e, new DefaultConnectionCursor(String.valueOf(index.incrementAndGet()))))
                .toList();

        var pageNumber = pageOfDataModels.getNumber();
        return new DefaultConnection<>(defaultEdges,
                new DefaultPageInfo(new DefaultConnectionCursor(String.valueOf(pageOfDataModels.getNumber())),
                        new DefaultConnectionCursor(String.valueOf(pageOfDataModels.hasNext() ? pageNumber + 1 : pageNumber)),
                        pageOfDataModels.hasPrevious(),
                        pageOfDataModels.hasNext()
                ));
    }
}
