package org.kexing.management.domin.model.mysql.jz;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lh
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(
                value = JzHuNeiExceptionReportedRecord.class,
                name = "JZ_HUNEI"),
        @JsonSubTypes.Type(
                value = JzHuWaiExceptionReportedRecord.class,
                name = "JZ_HUWAI"),
        @JsonSubTypes.Type(
                value = JzWeiKeOldExceptionReportedRecord.class,
                name = "JZ_WEIKE_OLD"),
})
@Schema(
        discriminatorProperty = "type",
        discriminatorMapping = {
                @DiscriminatorMapping(
                        schema = JzHuNeiExceptionReportedRecord.class,
                        value = "JZ_HUNEI"),
                @DiscriminatorMapping(
                        schema = JzHuWaiExceptionReportedRecord.class,
                        value = "JZ_HUWAI"),
                @DiscriminatorMapping(
                        schema = JzWeiKeOldExceptionReportedRecord.class,
                        value = "JZ_WEIKE_OLD"),
        })
public interface JzExceptionReportedRecord {
}
