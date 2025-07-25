package org.leisureup.info.spi.internal;

import static java.lang.Math.*;

import org.leisureup.info.spi.*;
import org.springframework.stereotype.*;

@Component
public class LambertConformalConicProjector {

    private final Data data = Data.getInstance();

    public LambertProjectionCord project(double x, double y) {

        double[] projection = lamcProj(x, y);
        int nx = (int) (projection[0] + 1.5);
        int ny = (int) (projection[1] + 1.5);

        return new LambertProjectionCord(nx, ny, x, y);
    }

    private double[] lamcProj(double lon, double lat) {

        double ra = tan(
                data.PI * 0.25 + lat * data.DEGRAD * 0.5
        );
        ra = data.re * data.sf / pow(ra, data.sn);

        double theta = lon * data.DEGRAD - data.olon;
        if (theta > data.PI) {
            theta -= 2.0 * data.PI;
        }
        if (theta < -data.PI) {
            theta += 2.0 * data.PI;
        }

        theta *= data.sn;

        return new double[]{
                (float) (ra * sin(theta)) + data.getXo(),
                (float) (data.ro - ra * cos(theta)) + data.getYo()
        };
    }
}

class Data {

    private static final MapData mapData = new MapData();
    private static Data instance;
    final double PI;
    final double DEGRAD;
    final double RADDEG;
    final double re;
    final double olon;
    final double olat;
    final double sn;
    final double sf;
    final double ro;

    @SuppressWarnings("DuplicateExpressions")
    public Data() {
        this.PI = Math.asin(1.0) * 2.0;
        this.DEGRAD = PI / 180.0;
        this.RADDEG = 180.0 / PI;

        this.re = mapData.Re / mapData.grid;
        this.olon = mapData.olon * DEGRAD;
        this.olat = mapData.olat * DEGRAD;

        double slat1 = mapData.slat1 * DEGRAD;
        double slat2 = mapData.slat2 * DEGRAD;

        {
            double sn = tan(PI * 0.25 + slat2 * 0.5) /
                        tan(PI * 0.25 + slat1 * 0.5);
            this.sn = log(cos(slat1) / cos(slat2)) / log(sn);
        }

        {
            double sf = tan(PI * 0.25 + slat1 * 0.5);
            this.sf = pow(sf, sn) * cos(slat1) / sn;
        }

        {
            double ro = tan(PI * 0.25 + olat * 0.5);
            this.ro = re * sf / pow(ro, sn);
        }
    }

    public static Data getInstance() {
        return instance == null ? (instance = new Data()) : instance;
    }

    public double getXo() {
        return mapData.xo;
    }

    public double getYo() {
        return mapData.yo;
    }

    private static class MapData {

        private final double Re;        // 지구 반경    (km)
        private final double grid;      // 격자 간격    (km)
        private final double slat1;     // 표준 위도 1  (degree)
        private final double slat2;     // 표준 위도 2  (degree)
        private final double olon;      // 기준점 경도   (degree)
        private final double olat;      // 기준점 위도   (degree)
        private final double xo;        // 기준점 x 좌표 (격자거리)
        private final double yo;        // 기준점 y 좌표 (격자거리)

        public MapData() {
            this.Re = 6371.00877;
            this.grid = 5.0;
            this.slat1 = 30.0;
            this.slat2 = 60.0;
            this.olon = 126.0;
            this.olat = 38.0;
            this.xo = 210 / this.grid;
            this.yo = 675 / this.grid;
        }
    }
}
