package com.sap.cloud.s4hana.examples.addressmgr.commands;

import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;
import com.sap.cloud.sdk.s4hana.connectivity.ErpCommand;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartner;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartnerAddress;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.BusinessPartnerService;

import org.slf4j.Logger;

public class GetSingleBusinessPartnerByIdCommand extends ErpCommand<BusinessPartner> {
    private static final Logger logger = CloudLoggerFactory.getLogger(GetSingleBusinessPartnerByIdCommand.class);

    private final BusinessPartnerService service;
    private final String id;

    public GetSingleBusinessPartnerByIdCommand(final BusinessPartnerService service, final String id) {
        super(GetSingleBusinessPartnerByIdCommand.class);
        this.service = service;
        this.id = id;
    }

    @Override
    protected BusinessPartner getFallback() {
        logger.warn("Fallback called because of exception", getExecutionException());
        return BusinessPartner.builder().businessPartner(id).build();
    }

    @Override
    protected BusinessPartner run() throws Exception {
        return service.getBusinessPartnerByKey(id)
            .select(
                BusinessPartner.BUSINESS_PARTNER,
                BusinessPartner.FIRST_NAME,
                BusinessPartner.LAST_NAME,
                BusinessPartner.IS_MALE,
                BusinessPartner.IS_FEMALE,
                BusinessPartner.CREATION_DATE,
                BusinessPartner.TO_BUSINESS_PARTNER_ADDRESS.select(
                    BusinessPartnerAddress.BUSINESS_PARTNER,
                    BusinessPartnerAddress.ADDRESS_ID,
                    BusinessPartnerAddress.COUNTRY,
                    BusinessPartnerAddress.CITY_NAME,
                    BusinessPartnerAddress.POSTAL_CODE,
                    BusinessPartnerAddress.STREET_NAME,
                    BusinessPartnerAddress.HOUSE_NUMBER
                )
            )
            .execute();
    }
}
