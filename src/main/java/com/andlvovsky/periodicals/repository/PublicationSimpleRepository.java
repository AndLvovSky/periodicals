package com.andlvovsky.periodicals.repository;

import com.andlvovsky.periodicals.model.Publication;

import java.util.Arrays;
import java.util.List;

public class PublicationSimpleRepository {

    public static List<Publication> publications = Arrays.asList(
            new Publication("aaa", 1, 1f, "-"),
            new Publication("bbbb", 3, 4.5f, "-"),
            new Publication("cccccc", 2, 4f, "-")
    );

}
