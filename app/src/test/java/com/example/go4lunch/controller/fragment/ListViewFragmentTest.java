package com.example.go4lunch.controller.fragment;

import org.junit.Test;

import static org.junit.Assert.*;

public class ListViewFragmentTest {
    ListViewFragment fragment = new ListViewFragment();
    @Test
    public void getDisplayDateFormatTest() {
        String actual = fragment.nameFormatter("Restaurant name with-should not be displayed");
        String expected = "Restaurant name with";
        assertEquals(expected, actual);

        actual = fragment.nameFormatter("Restaurant name with,should not be displayed");
        expected = "Restaurant name with";
        assertEquals(expected, actual);

        actual = fragment.userRatingTotalFormatter("264");
        expected = "(264)";
        assertEquals(expected, actual);

        actual = fragment.distanceFormatter(16.77546);
        expected = "17 m";
        assertEquals(expected, actual);

    }
}