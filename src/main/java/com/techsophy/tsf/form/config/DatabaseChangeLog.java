package com.techsophy.tsf.form.config;

import com.techsophy.tsf.form.entity.FormAuditDefinition;
import com.techsophy.tsf.form.entity.FormDefinition;
import com.techsophy.tsf.form.repository.FormDefinitionAuditRepository;
import com.techsophy.tsf.form.repository.FormDefinitionRepository;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;
import static com.techsophy.tsf.form.constants.FormModelerConstants.*;

@ChangeUnit(id=TP_APP_FORM, order =ORDER_1,systemVersion=SYSTEM_VERSION_1)
@RequiredArgsConstructor
public class DatabaseChangeLog
{
    private final FormDefinitionRepository formDefinitionRepository;
    private final FormDefinitionAuditRepository formDefinitionAuditRepository;
    List<FormDefinition> formDefinitionList;
    List<FormAuditDefinition> formAuditDefinitionList;

    @Execution
    public void changeSetFormDefinition()
    {
        formDefinitionList =formDefinitionRepository.findAll();
        List<FormDefinition> modificationFormList=formDefinitionList;
        modificationFormList.forEach(
                formDefinition ->
                {
                    if(String.valueOf(formDefinition.getComponents()).equals(NULL))
                    {
                        formDefinition.setComponents(Map.of());
                    }
                    if(String.valueOf(formDefinition.getProperties()).equals(NULL))
                    {
                        formDefinition.setProperties(Map.of());
                    }
                    if(String.valueOf(formDefinition.getIsDefault()).equals(NULL))
                    {
                        formDefinition.setIsDefault(false);
                    }
                });
        formDefinitionRepository.saveAll(modificationFormList);

        formAuditDefinitionList=formDefinitionAuditRepository.findAll();
        List<FormAuditDefinition> modificationAuditFormList =formAuditDefinitionList;
        modificationAuditFormList.forEach(
                formAuditDefinition ->
                {
                    if(String.valueOf(formAuditDefinition.getComponents()).equals(NULL))
                    {
                        formAuditDefinition.setComponents(Map.of());
                    }
                    if(String.valueOf(formAuditDefinition.getProperties()).equals(NULL))
                    {
                        formAuditDefinition.setProperties(Map.of());
                    }
                    if(String.valueOf(formAuditDefinition.getIsDefault()).equals(NULL))
                    {
                        formAuditDefinition.setIsDefault(false);
                    }
                });
        formDefinitionAuditRepository.saveAll(modificationAuditFormList);
    }

    @RollbackExecution
    public void rollback()
    {
     formDefinitionRepository.saveAll(formDefinitionList);
     formDefinitionAuditRepository.saveAll(formAuditDefinitionList);
    }
}
