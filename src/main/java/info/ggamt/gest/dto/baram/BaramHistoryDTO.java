package info.ggamt.gest.dto.baram;

import java.util.List;

import info.ggamt.gest.domain.baram.DayHistory;
import info.ggamt.gest.domain.baram.TimeHistory;

public class BaramHistoryDTO {
    List<DayHistory> dayHistoryList;
    List<TimeHistory> timeHistoryList;
    public List<DayHistory> getDayHistoryList() {
        return dayHistoryList;
    }
    public void setDayHistoryList(List<DayHistory> dayHistoryList) {
        this.dayHistoryList = dayHistoryList;
    }
    public List<TimeHistory> getTimeHistoryList() {
        return timeHistoryList;
    }
    public void setTimeHistoryList(List<TimeHistory> timeHistoryList) {
        this.timeHistoryList = timeHistoryList;
    }
}
