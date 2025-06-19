package org.leisureup.location.internal.dto.api;

import lombok.*;
import org.leisureup.location.internal.service.*;

/**
 * 공통정보 응답을 위한 class
 *
 * @see CommonInfo
 * @see TourApiClient#getCommonInfo
 */
@NoArgsConstructor
public class GetCommonInfoResponse extends TourApiResponse<CommonInfo> {

}
