package com.user.user.util;

import java.io.*;
import java.time.LocalDate;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import com.user.user.domain.athlete.AthletewDesc;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.user.user.domain.athleteDesc.AthleteDesc;

public class CsvGenerator {
    public static String exportAthleteToCsv(List<AthletewDesc> athleteswDescs, UUID teamId) throws IOException {
        String[][] athleteDescMatrixData = new String[athleteswDescs.size()][7];

        for (int i = 0; i < athleteswDescs.size(); i++) {
            AthleteDesc athleteDesc = athleteswDescs.get(i).athleteDesc();
            Integer idade = LocalDate.now().getYear() - athleteswDescs.get(i).athlete().getBirthDate().getYear();

            athleteDescMatrixData[i][0] = athleteswDescs.get(i).athlete().getFirstName();
            athleteDescMatrixData[i][1] = athleteswDescs.get(i).athlete().getLastName();
            athleteDescMatrixData[i][2] = athleteswDescs.get(i).athlete().getCategory();
            athleteDescMatrixData[i][3] = athleteDesc.getPosition();
            athleteDescMatrixData[i][4] = athleteDesc.getHeight().toString();
            athleteDescMatrixData[i][5] = athleteDesc.getWeight().toString();
            athleteDescMatrixData[i][6] = idade.toString();
        }

        String[] columnNames = { "Nome", "Sobrenome", "Categoria", "Posição", "Altura", "Peso", "Idade" };
        StringBuilder csvHeader = new StringBuilder();
        for (String columnName : columnNames) {
            csvHeader.append(columnName).append(",");
        }
        csvHeader.deleteCharAt(csvHeader.length() - 1);

        for (int i = 0; i < athleteswDescs.size(); i++) {
            StringBuilder csvRow = new StringBuilder();
            for (int j = 0; j < athleteDescMatrixData[i].length; j++) {
                csvRow.append(athleteDescMatrixData[i][j]).append(",");
            }
            csvRow.deleteCharAt(csvRow.length() - 1);
        }

        int[] columnWidths = calculateColumnWidths(athleteDescMatrixData);

        String csvFilePath = CodeGenerator.codeGen(6, true) + teamId.toString() + ".csv";

        try (FileOutputStream file = new FileOutputStream(csvFilePath);
                OutputStreamWriter fileWriter = new OutputStreamWriter(file, StandardCharsets.UTF_8);
                CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withDelimiter(';'))) {
            for (String[] userData : athleteDescMatrixData) {
                String[] formattedData = formatDataWithColumnWidths(userData, columnWidths);

                csvPrinter.printRecord((Object[]) formattedData);
            }

            return csvFilePath;
        }
    }

    private static int[] calculateColumnWidths(String[][] data) {
        int[] columnWidths = new int[data[0].length];
        for (String[] row : data) {
            for (int i = 0; i < row.length; i++) {
                columnWidths[i] = Math.max(columnWidths[i], row[i].length());
            }
        }
        return columnWidths;
    }

    private static String[] formatDataWithColumnWidths(String[] data, int[] columnWidths) {
        String[] formattedData = new String[data.length];
        for (int i = 0; i < data.length; i++) {
            formattedData[i] = String.format("%-" + columnWidths[i] + "s", data[i]);
        }
        return formattedData;
    }

}
