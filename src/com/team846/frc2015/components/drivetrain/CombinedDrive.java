package com.team846.frc2015.components.drivetrain;

import java.util.Arrays;
import java.util.List;

public class CombinedDrive extends DriveStyle {
    List<DriveStyle> substyles;

    public CombinedDrive(List<DriveStyle> substyles) {
        this.substyles = substyles;
    }

    public CombinedDrive(DriveStyle... substyles) {
        this.substyles = Arrays.asList(substyles);
    }

    @Override
    public DriveSpeed getSpeeds() {
        return substyles.stream().
            map(DriveStyle::getSpeeds).
            reduce(DriveSpeed::plus).get();
    }
}
