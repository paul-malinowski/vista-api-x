package gov.va.octo.vista.api.enumeration.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import gov.va.octo.vista.api.enumeration.AppConfigTypeEnum;

@Converter(autoApply = true)
public class AppConfigTypeEnumConverter implements AttributeConverter<AppConfigTypeEnum, Long> {

    @Override
    public Long convertToDatabaseColumn(AppConfigTypeEnum attr) {
        return attr.getId();
    }

    @Override
    public AppConfigTypeEnum convertToEntityAttribute(Long data) {
        return AppConfigTypeEnum.valueOf(data);
    }

}
