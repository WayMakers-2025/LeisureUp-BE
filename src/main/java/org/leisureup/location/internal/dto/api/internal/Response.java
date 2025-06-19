
package org.leisureup.location.internal.dto.api.internal;

/**
 * TourApi 응답은 대게 다음 모양처럼 구성됨
 *
 * <pre>
 *     {@code
 *      {
 *          "response": {
 *              "header": {
 *                  "resultCode": "0000",
 *                  "resultMsg": "OK"
 *              },
 *              "body": {
 *                  "items": {
 *                      "item": [
 *                          { ... },
 *                          { ... }
 *                      ]
 *                  },
 *                  "numOfRows": 2,
 *                  "pageNo": 1,
 *                  "totalCount": 2
 *              }
 *          }
 *      }
 *     }
 * </pre>
 * <p>
 * 만약 요청에 대한 정보 없으면 아래처럼 구성됨
 *
 * <pre>
 *     {@code
 *      {
 *          "response": {
 *              "header": {
 *                  "resultCode": "0000",
 *                  "resultMsg": "OK"
 *              },
 *              "body": {
 *                  "items": "",
 *                  "numOfRows": 0,
 *                  "pageNo": 1,
 *                  "totalCount": 0
 *              }
 *          }
 *      }
 *     }
 * </pre>
 */
public record Response<I>(
        Header header,
        Body<I> body
) {

    public boolean isSuccess() {
        return header != null && body != null && header.isSuccess();
    }
}


