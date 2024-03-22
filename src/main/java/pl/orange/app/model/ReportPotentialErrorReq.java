package pl.orange.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReportPotentialErrorReq {
    String sn;
    String testingStation;
    String date;
}
