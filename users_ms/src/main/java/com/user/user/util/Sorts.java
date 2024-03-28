package com.user.user.util;

import java.util.ArrayList;
import java.util.List;

import com.user.user.domain.athlete.Athlete;

public class Sorts {
    public static void mergeSortAthletesByAgeAsc(List<Athlete> a, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            mergeSortAthletesByAgeAsc(a, left, mid);
            mergeSortAthletesByAgeAsc(a, mid + 1, right);

            mergeAthletes(a, left, mid, right);
        }
    }

    private static void mergeAthletes(List<Athlete> a, int left, int mid, int right) {
        int i = left, j = mid + 1;

        List<Athlete> temp = new ArrayList<>(right - left + 1);

        while (i <= mid && j <= right) {
            if (a.get(i).getBirthDate().compareTo(a.get(j).getBirthDate()) <= 0) {
                temp.add(a.get(i++));
            } else {
                temp.add(a.get(j++));
            }
        }

        while (i <= mid) {
            temp.add(a.get(i++));
        }

        while (j <= right) {
            temp.add(a.get(j++));
        }

        for (int x = left; x <= right; x++) {
            a.set(x, temp.get(x - left));
        }
    }
}