package com.user.user.utils;

import java.util.ArrayList;
import java.util.List;

import com.user.user.domains.athlete.Athlete;

public class Sorts {
    public static void mergeSortAthletesByAgeAsc(List<Athlete> a, int size) {
        if (size < 2)
            return;

        int mid = size / 2;

        List<Athlete> l = new ArrayList<>(mid);
        List<Athlete> r = new ArrayList<>(size - mid);

        for (int i = 0; i < mid; i++) {
            l.set(i, a.get(i));
        }

        for (int i = mid; i < size; i++) {
            r.set(i - mid, a.get(i));
        }

        mergeSortAthletesByAgeAsc(l, mid);
        mergeSortAthletesByAgeAsc(r, mid);

        mergeAthletes(a, l, r, mid, size - mid);
    }

    public static void mergeAthletes(List<Athlete> a, List<Athlete> l, List<Athlete> r, int left, int right) {
        int i = 0, j = 0, k = 0;

        while (i < left && j < right) {
            if (l.get(i).getBirthDate().isBefore(r.get(j).getBirthDate())) {
                a.set(k++, l.get(i++));
            } else {
                a.set(k++, r.get(j++));
            }
        }

        while (i < left) {
            a.set(k++, l.get(j++));
        }

        while (j < right) {
            a.set(k++, r.get(j++));
        }
    }
}