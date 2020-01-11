package com.example.go4lunch.controller.fragment;

import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
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

    }

    @Test
    public void getDistanceFromTest() {
        String actual = fragment.distanceFormatter(16.77546);
        String expected = "17 m";
        assertEquals(expected, actual);
    }

    @Test
    public void getUserRatingTest() {
        String actual = fragment.userRatingTotalFormatter(264);
        String expected = "(264)";
        assertEquals(expected, actual);
    }

    @Test
    public void getWebsiteUrlTest() {
        String actual = fragment.websiteFormatter(Uri.parse("http://test.fr/"));
        String expected = "http://test.fr/";
        assertEquals(expected, actual);
    }
}