package com.example.swimappuiframework.data;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataHandler {
    public DataHandler() {}

    public List<List<Double>> arrayToList(double[][] array) {
        List<List<Double>> list = new ArrayList<>();
        for (double[] a : array) {
            List<Double> tempList = new ArrayList<>();
            for (double v : a) {
                tempList.add(v);
            }
            list.add(tempList);
        }
        return list;
    }

    public List<Integer> identifyTurns(List<Double> zAcc) {
        List<Integer> turnIndx = new ArrayList<>();
        boolean turning = false;
        int lastTurn = -50;
        if (zAcc != null && !zAcc.isEmpty()) {
            for (int i = 0; i < zAcc.size(); i++) {
                if (!turning && i > lastTurn + 50 && zAcc.get(i) > 2000) {
                    turning = true;
                    turnIndx.add(i);
                    lastTurn = i;
                }
                else if (turning && zAcc.get(i) < 1000) {
                    turning = false;
                }
            }
        }
        return turnIndx;
    }

    public List<Integer> identifyStrokes(List<Double> yAcc) {
        List<Integer> strokes = new ArrayList<>();
        boolean isCatch = false;
        if (yAcc != null && !yAcc.isEmpty()) {
            for (int i = 0; i < yAcc.size(); i++) {
                if (!isCatch && i > 35 && yAcc.get(i) > 2000) {
                    isCatch = true;
                    strokes.add(i);
                }
                if (isCatch && yAcc.get(i) < 1000) {
                    isCatch = false;
                }
            }
        }
        return strokes;
    }

    public List<List<Double>> parseStrokes(List<Double> inData, List<Integer> strokes, List<Integer> turns) {
        List<List<Double>> strokeVals = new ArrayList<>();
        int prevStroke = strokes.get(0);
        int turnIdx = 0;
        if (inData != null && !inData.isEmpty() && strokes != null && !strokes.isEmpty() && turns != null) {
            for (int i = 1; i < strokes.size(); i++) { // Always ignore last stroke
                int currStroke = strokes.get(i);

                // Case where turn happens during that stroke: skip
                if (turnIdx < turns.size() && prevStroke < turns.get(turnIdx) && currStroke > turns.get(turnIdx)) {
                    turnIdx++;
                    prevStroke = currStroke;
                    continue;
                }
                strokeVals.add(new ArrayList<>());
                for (int j = prevStroke; j < currStroke; j++) {
                    strokeVals.get(strokeVals.size() - 1).add(inData.get(j));
                }
                prevStroke = currStroke;
            }
        }
        return strokeVals;
    }

    public List<List<Double>> normalizeStrokes(List<List<Double>> inVals) {
        List<List<Double>> interpolatedValues = new ArrayList<>();
        int desiredLength = 41;
        for (List<Double> stroke : inVals) {
            interpolatedValues.add(new ArrayList<>());

            double step = (double) (stroke.size() - 1) / (desiredLength - 1);
            for (int i = 0; i < desiredLength; i++) {
                double index = i * step;
                int lowerIndex = (int) Math.floor(index);
                int upperIndex = (int) Math.ceil(index);

                if (upperIndex >= stroke.size()) {
                    interpolatedValues.get(interpolatedValues.size() - 1).add(stroke.get(stroke.size() - 1));
                    continue;
                }

                double lowerValue = stroke.get(lowerIndex);
                double upperValue = stroke.get(upperIndex);

                double interpolatedValue = lowerValue + (index - lowerIndex) * (upperValue - lowerValue);
                interpolatedValues.get(interpolatedValues.size() - 1).add(interpolatedValue);
            }
        }
        return interpolatedValues;
    }

    public List<Double> averageStrokes(List<List<Double>> normVals) {
        int length = normVals.get(0).size();
        int numOfLists = normVals.size();
        List<Double> averagedStroke = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            double sum = 0.0;

            for (List<Double> stroke : normVals) {
                sum += stroke.get(i);
            }

            double average = sum / numOfLists;
            averagedStroke.add(average);
        }
        return averagedStroke;
    }

    public List<Double> calcFullCorrelation(List<List<Double>> data, List<Double> average) {
        List<Double> correlations = new ArrayList<>();
        if (data != null && !data.isEmpty()) {
            for (List<Double> stroke : data) {
                correlations.add(calculateCorrelation(stroke, average));
            }
        }
        return correlations;
    }

    public double calculateCorrelation(List<Double> normStroke, List<Double> average) {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        return Math.abs(correlation.correlation(normStroke.stream().mapToDouble(Double::doubleValue).toArray(),
                average.stream().mapToDouble(Double::doubleValue).toArray()));
    }
}
