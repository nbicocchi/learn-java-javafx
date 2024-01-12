/*
 * The MIT License
 *
 * Copyright 2020 Logesh0304.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nbicocchi.javafx.regex;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Match information to be show in the table
 */
public class MatchInformation {
    private final SimpleIntegerProperty matchNumber;
    private final SimpleStringProperty indexRange;
    private final SimpleStringProperty match;

    public MatchInformation(int matchNumber, String indexRange, String match) {
        this.matchNumber = new SimpleIntegerProperty(matchNumber);
        this.indexRange = new SimpleStringProperty(indexRange);
        this.match = new SimpleStringProperty(match);
    }

    public int getMatchNumber() {
        return matchNumber.get();
    }

    public void setMatchNumber(int matchNumber) {
        this.matchNumber.set(matchNumber);
    }

    public String getIndexRange() {
        return indexRange.get();
    }

    public void setIndexRange(String indexRange) {
        this.indexRange.set(indexRange);
    }

    public String getMatch() {
        return match.get();
    }

    public void setMatch(String match) {
        this.match.set(match);
    }
}
