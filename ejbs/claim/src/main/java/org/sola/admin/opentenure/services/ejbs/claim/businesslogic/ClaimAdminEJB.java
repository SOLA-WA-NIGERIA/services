package org.sola.admin.opentenure.services.ejbs.claim.businesslogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.sola.common.RolesConstants;
import org.sola.admin.opentenure.services.ejbs.claim.entities.ClaimStatus;
import org.sola.admin.opentenure.services.ejbs.claim.entities.FieldConstraintType;
import org.sola.admin.opentenure.services.ejbs.claim.entities.FieldType;
import org.sola.admin.opentenure.services.ejbs.claim.entities.FieldValueType;
import org.sola.admin.opentenure.services.ejbs.claim.entities.FormTemplate;
import org.sola.services.common.ejbs.AbstractEJB;
import org.sola.services.common.repository.CommonSqlProvider;
import org.sola.admin.services.ejb.system.businesslogic.SystemAdminEJBLocal;
import org.sola.admin.services.ejbs.admin.businesslogic.AdministratorEJBLocal;

/**
 * Implements methods to manage the claim and it's related objects
 */
@Stateless
@EJB(name = "java:app/ClaimAdminEJBLocal", beanInterface = ClaimAdminEJBLocal.class)
public class ClaimAdminEJB extends AbstractEJB implements ClaimAdminEJBLocal {

    @EJB
    SystemAdminEJBLocal systemEjb;

    @EJB
    AdministratorEJBLocal adminEjb;

    /**
     * Sets the entity package for the EJB to
     * Claim.class.getPackage().getName(). This is used to restrict the save and
     * retrieval of Code Entities.
     *
     * @see AbstractEJB#getCodeEntity(java.lang.Class, java.lang.String,
     * java.lang.String) AbstractEJB.getCodeEntity
     * @see AbstractEJB#getCodeEntityList(java.lang.Class, java.lang.String)
     * AbstractEJB.getCodeEntityList
     * @see
     * AbstractEJB#saveCodeEntity(org.sola.services.common.repository.entities.AbstractCodeEntity)
     * AbstractEJB.saveCodeEntity
     */
    @Override
    protected void postConstruct() {
        setEntityPackage(ClaimStatus.class.getPackage().getName());
    }

    /**
     * Returns list of claim statuses
     *
     * @param languageCode
     * @return
     */
    @Override
    public List<ClaimStatus> getClaimStatuses(String languageCode) {
        return getRepository().getCodeList(ClaimStatus.class, languageCode);
    }

    /**
     * Returns claim status by code
     *
     * @param code Code of status
     * @param languageCode Locale code
     * @return
     */
    @Override
    public ClaimStatus getClaimStatus(String code, String languageCode) {
        return getRepository().getCode(ClaimStatus.class, code, languageCode);
    }

    @Override
    //@RolesAllowed({RolesConstants.CS_ACCESS_CS})
    public List<FormTemplate> getFormTemplates(String languageCode) {
        HashMap params = new HashMap();
        if (languageCode != null) {
            params.put(CommonSqlProvider.PARAM_LANGUAGE_CODE, languageCode);
        }
        return getRepository().getEntityList(FormTemplate.class, params);
    }

    @Override
    //@RolesAllowed({RolesConstants.CS_ACCESS_CS})
    public FormTemplate getFormTemplate(String templateName, String languageCode) {
        if (languageCode != null) {
            return getRepository().getEntity(FormTemplate.class, templateName, languageCode);
        } else {
            return getRepository().getEntity(FormTemplate.class, templateName);
        }
    }

    @Override
    @RolesAllowed({RolesConstants.CS_ACCESS_CS})
    public boolean checkFormTemplateHasPayload(String formName) {
        if (formName == null) {
            return false;
        }

        String sql = "select count(1) > 0 as result from opentenure.form_payload where form_template_name=#{formName}";
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, sql);
        params.put("formName", formName);

        ArrayList<HashMap> result = getRepository().executeSql(params);
        if (result == null || result.size() < 1) {
            return false;
        } else {
            return result.get(0).get("result") != null && Boolean.parseBoolean(result.get(0).get("result").toString());
        }
    }

    @Override
    //@RolesAllowed({RolesConstants.CS_ACCESS_CS})
    public FormTemplate getDefaultFormTemplate(String languageCode) {
        HashMap params = new HashMap();
        if (languageCode != null) {
            params.put(CommonSqlProvider.PARAM_LANGUAGE_CODE, languageCode);
        }
        return getRepository().getEntity(FormTemplate.class, "is_default = 't'", params);
    }

    @Override
    public List<FieldType> getFieldTypes(String languageCode) {
        return getRepository().getCodeList(FieldType.class, languageCode);
    }

    @Override
    public List<FieldValueType> getFieldValueTypes(String languageCode) {
        return getRepository().getCodeList(FieldValueType.class, languageCode);
    }

    @Override
    public List<FieldConstraintType> getFieldConstraintTypes(String languageCode) {
        return getRepository().getCodeList(FieldConstraintType.class, languageCode);
    }

    @Override
    @RolesAllowed({RolesConstants.ADMIN_MANAGE_SETTINGS})
    public FormTemplate saveFormTemplate(FormTemplate form) {
        return getRepository().saveEntity(form);
    }
}
