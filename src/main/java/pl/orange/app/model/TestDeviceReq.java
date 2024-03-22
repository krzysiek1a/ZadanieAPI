package pl.orange.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TestDeviceReq {
    String sn;
    String testingStation;
}
