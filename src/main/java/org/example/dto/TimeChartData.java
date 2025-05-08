package org.example.dto;

import java.util.Date;

public interface TimeChartData {
    public String getProjectName();
    public Date getStartDate();
    public Date getEndDate();
    public String getStatus();
    public int getProgress();
}
