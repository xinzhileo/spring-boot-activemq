package org.tongwoo.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.tongwoo.util.ConcurrentQueue;
import org.tongwoo.util.MyJsonObject;

import java.sql.SQLException;

public class ProcessStaticRunning implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessStaticRunning.class);
    private static JdbcTemplate jdbcTemplate;
    private ConcurrentQueue queue;
    private static volatile Integer count = 0;

    @Override
    public void run() {
        while(true){
            MyJsonObject obj = (MyJsonObject) queue.take();
            if(obj == null) continue;
            saveMsg(obj);
            commit(10000);
        }
    }

    public static void commit(int i){
        try {
            if(count<i) return;
            jdbcTemplate.getDataSource().getConnection().commit();
            count = 0;
            LOG.info("static data count have arrived 10000 so commit");
        } catch (SQLException e) {
            LOG.warn("{}", e);
        }
    }

    private void saveMsg(MyJsonObject obj) {
//        LOG.info("消费者RDS已经收到数据：" + obj.toJSONString());
        if(!obj.containsKey("InfoFlag"))
        {
            LOG.info("无法判断是什么数据："+ obj.toJSONString());
            return;
        }
        String strSql = "";
        String strInfoFlag = obj.getString("InfoFlag");

//			if(strInfoFlag.equals("positionDriver"))
//			{
//				//print("消费者RDS已经收到驾驶员定位数据：" + strMsg);
//				String strCompanyId = obj.getString("CompanyId");
//				String strLicenseId = obj.getString("LicenseId");
//				int nDriverRegionCode = obj.getIntValue("DriverRegionCode");
//				String strVehicleNo = obj.getString("VehicleNo");
//				String strPositionTime = obj.getLong("PositionTime").toString();
//				long dLongitude = obj.getLongValue("Longitude");
//				long dLatitude = obj.getLongValue("Latitude");
//				int nEncrypt = 0;
//				if(obj.containsKey("Encrypt"))
//				{
//					nEncrypt = obj.getIntValue("Encrypt");
//				}
//				int nDirection = 0;
//				if(obj.containsKey("Direction"))
//				{
//					nDirection = obj.getIntValue("Direction");
//				}
//				double dElevation = 0;
//				if(obj.containsKey("Elevation"))
//				{
//					dElevation = obj.getDoubleValue("Elevation");
//				}
//				double dSpeed = 0;
//				if(obj.containsKey("Speed"))
//				{
//					dSpeed = obj.getDoubleValue("Speed");
//				}
//				int nBizStatus = 0;
//				if(obj.containsKey("BizStatus"))
//				{
//					nBizStatus = obj.getIntValue("BizStatus");
//				}
//				String strOrderId = obj.getString("OrderId");
//				strSql = "insert into tb_PositionDriver(CompanyId, LicenseId, DriverRegionCode, VehicleNo, PositionTime, Longitude, Latitude, EncryptBS, Direction,"
//						+ "Elevation, Speed, BizStatus, OrderId) values ('"
//						+ strCompanyId + "', '"
//						+ strLicenseId + "', "
//						+ nDriverRegionCode + ", '"
//						+ strVehicleNo + "', '"
//						+ strPositionTime + "', "
//						+ dLongitude + ", "
//						+ dLatitude + ", "
//						+ nEncrypt + ", "
//						+ nDirection + ", "
//						+ dElevation + ", "
//						+ dSpeed + ", "
//						+ nBizStatus + ", '"
//						+ strOrderId + "') on duplicate key update CompanyId = '"
//						+ strCompanyId + "', VehicleNo = '"
//						+ strVehicleNo + "', DriverRegionCode = "
//						+ nDriverRegionCode + ", PositionTime = '"
//						+ strPositionTime + "', Longitude = "
//						+ dLongitude + ", Latitude = "
//						+ dLatitude + ", EncryptBS = "
//						+ nEncrypt + ", Direction = "
//						+ nDirection + ", Elevation = "
//						+ dElevation + ", Speed = "
//						+ dSpeed + ", BizStatus = "
//						+ nBizStatus + ", OrderId = '"
//						+ strOrderId + "'";
//				jdbcTemplate.batchUpdate(strSql);
//			}
//			else if(strInfoFlag.equals("positionVehicle"))
//			{
//				//print("消费者RDS已经收到车辆定位数据：" + strMsg);
//				String strCompanyId = obj.getString("CompanyId");
//				String strVehicleNo = obj.getString("VehicleNo");
//				int nVehicleRegionCode = obj.getIntValue("VehicleRegionCode");
//				String strPositionTime = obj.getLong("PositionTime").toString();
//				long dLongitude = obj.getLongValue("Longitude");
//				long dLatitude = obj.getLongValue("Latitude");
//				double dSpeed = 0;
//				if(obj.containsKey("Speed"))
//				{
//					dSpeed = obj.getDoubleValue("Speed");
//				}
//				int nDirection = 0;
//				if(obj.containsKey("Direction"))
//				{
//					nDirection = obj.getIntValue("Direction");
//				}
//				double dElevation = 0;
//				if(obj.containsKey("Elevation"))
//				{
//					dElevation = obj.getDoubleValue("Elevation");
//				}
//				double dMileage = 0;
//				if(obj.containsKey("Mileage"))
//				{
//					dMileage = obj.getDoubleValue("Mileage");
//				}
//				int nEncrypt = 0;
//				if(obj.containsKey("Encrypt"))
//				{
//					nEncrypt = obj.getIntValue("Encrypt");
//				}
//				int nWarnStatus = 0;
//				if(obj.containsKey("WarnStatus"))
//				{
//					nWarnStatus = obj.getIntValue("WarnStatus");
//				}
//				int nVehStatus= 0;
//				if(obj.containsKey("VehStatus"))
//				{
//					nVehStatus= obj.getIntValue("VehStatus");
//				}
//
//				int nBizStatus= 0;
//				if(obj.containsKey("BizStatus"))
//				{
//					nBizStatus = obj.getIntValue("BizStatus");
//				}
//
//				String strOrderId = obj.getString("OrderId");
//				strSql = "insert into tb_positionVehicle(CompanyId, VehicleNo, VehicleRegionCode, PositionTime, Longitude, Latitude, Speed, Direction, Elevation,"
//						+ "Mileage, EncryptBS, WarnStatus, VehStatus, BizStatus, OrderId) values ('"
//						+ strCompanyId + "', '"
//						+ strVehicleNo + "', "
//						+ nVehicleRegionCode + ", '"
//						+ strPositionTime + "', "
//						+ dLongitude + ", "
//						+ dLatitude + ", "
//						+ dSpeed + ", "
//						+ nDirection + ", "
//						+ dElevation + ", "
//						+ dMileage + ", "
//						+ nEncrypt + ", "
//						+ nWarnStatus + ", "
//						+ nVehStatus + ", "
//						+ nBizStatus + ", '"
//						+ strOrderId + "') on duplicate key update CompanyId = '"
//						+ strCompanyId + "', VehicleRegionCode = "
//						+ nVehicleRegionCode + ", PositionTime = '"
//						+ strPositionTime + "', Longitude = "
//						+ dLongitude + ", Latitude = "
//						+ dLatitude + ", Speed = "
//						+ dSpeed + ", Direction = "
//						+ nDirection + ", Elevation = "
//						+ dElevation + ", Mileage = "
//						+ dMileage + ", EncryptBS = "
//						+ nEncrypt + ", WarnStatus = "
//						+ nWarnStatus + ", VehStatus = "
//						+ nVehStatus + ", BizStatus = "
//						+ nBizStatus + ", OrderId = '"
//						+ strOrderId + "'";
//
//
//				jdbcTemplate.batchUpdate(strSql);
//			}
        if(strInfoFlag.equals("baseInfoCompany"))
        {
            String strCompanyId = obj.getString("CompanyId");
            String strCompanyName = obj.getString("CompanyName");
            String strIdentifier = obj.getString("Identifier");
            int nAddress = obj.getIntValue("Address");
            String strBusinessScope = obj.getString("BusinessScope");
            String strContactAddress = obj.getString("ContactAddress");
            String strEconomicType = obj.getString("EconomicType");
            String strRegCapital = obj.getString("RegCapital");
            String strLegalName = obj.getString("LegalName");
            String strLegalID = obj.getString("LegalID");
            String strLegalPhone = obj.getString("LegalPhone");

            String strLegalPhoto = "";
            if(obj.containsKey("LegalPhoto"))
            {
                strLegalPhoto = obj.getString("LegalPhoto");
            }

            int nState = obj.getIntValue("State");
            int nFlag = obj.getIntValue("Flag");
            String strUpdateTime = obj.getLong("UpdateTime").toString();
            if(nFlag == 1)
            {
                strSql = "insert into tb_company (CompanyId, CompanyName, Identifier, Address, BusinessScope, ContactAddress, EconomicType, RegCapital, "
                        + "LegalName, LegalID, LegalPhone, LegalPhoto, State, Flag, UpdateTime) values ('"
                        + strCompanyId + "', '"
                        + strCompanyName + "', '"
                        + strIdentifier +"', "
                        + nAddress + ", '"
                        + strBusinessScope + "', '"
                        + strContactAddress + "', '"
                        + strEconomicType + "', '"
                        + strRegCapital + "', '"
                        + strLegalName + "', '"
                        + strLegalID + "', '"
                        + strLegalPhone + "', '"
                        + strLegalPhoto + "', "
                        + nState + ", "
                        + nFlag + ", '"
                        + strUpdateTime + "')";
            }
            else if(nFlag == 2)
            {
                strSql = "update tb_company set CompanyId = '" + strCompanyId + "', CompanyName = '"
                        + strCompanyName + "', Identifier = '" + strIdentifier + "', Address = " + nAddress
                        + ", BusinessScope = '" + strBusinessScope + "', ContactAddress = '" + strContactAddress + "', EconomicType = '"
                        + strEconomicType + "', RegCapital = '" + strRegCapital + "', LegalName = '" + strLegalName + "', LegalID = '"
                        + strLegalID + "', LegalPhone = '" + strLegalPhone + "', State = " + nState + ", Flag = "
                        + nFlag + ", UpdateTime = '" + strUpdateTime + "' where CompanyId = '" + strCompanyId + "'" ;
            }
            else if(nFlag == 3)
            {
                strSql = "delete from tb_company where CompanyId = '"+ strCompanyId + "'";
            }

            //
            jdbcTemplate.batchUpdate(strSql);
        }
        else if(strInfoFlag.equals("baseInfoCompanyStat"))
        {
            String strCompanyId = obj.getString("CompanyId");
            int nVehicleNum = obj.getIntValue("VehicleNum");
            int nDriverNum = obj.getIntValue("DriverNum");
            int nFlag = obj.getIntValue("Flag");
            String strUpdateTime = obj.getLong("UpdateTime").toString();
            if(nFlag == 1)
            {
                strSql = "insert into tb_companystat(CompanyId, VehicleNum, DriverNum, Flag, UpdateTime) values ('"
                        + strCompanyId + "', "
                        + nVehicleNum + ", "
                        + nDriverNum + ", "
                        + nFlag + ", '"
                        + strUpdateTime + "')";
            }
            else if(nFlag == 2)
            {
                strSql = "update tb_companystat set CompanyId = '" + strCompanyId + "', VehicleNum = "
                        + nVehicleNum + ", DriverNum = " + nDriverNum + ", Flag = " + nFlag + ", UpdateTime = '"
                        + strUpdateTime + "' where CompanyId = '"+ strCompanyId + "'";
            }
            else if(nFlag == 3)
            {
                strSql = "delete from tb_companystat where CompanyId = '"+ strCompanyId + "'";
            }
            //
            jdbcTemplate.batchUpdate(strSql);
        }
        else if(strInfoFlag.equals("baseInfoCompanyPay"))
        {
            String strCompanyId = obj.getString("CompanyId");
            String strPayName = obj.getString("PayName");
            String strPayId = obj.getString("PayId");
            String strPayType = obj.getString("PayType");
            String strPayScope = obj.getString("PayScope");
            String strPrepareBank = obj.getString("PrepareBank");
            int nCountDate = obj.getIntValue("CountDate");
            int nState = obj.getIntValue("State");
            int nFlag = obj.getIntValue("Flag");
            String strUpdateTime = obj.getLong("UpdateTime").toString();
            if(nFlag == 1)
            {
                strSql = "insert into tb_companypay(CompanyId, PayName, PayId, PayType, PayScope, PrepareBank, CountDate, State, Flag, UpdateTime) values ('"
                        + strCompanyId + "', '"
                        + strPayName + "', '"
                        + strPayId + "', '"
                        + strPayType + "', '"
                        + strPayScope + "', '"
                        + strPrepareBank + "', "
                        + nCountDate + ", "
                        + nState + ", "
                        + nFlag + ", '"
                        + strUpdateTime + "')";
            }
            else if(nFlag == 2)
            {
                strSql = "update tb_companypay set CompanyId = '" + strCompanyId + "', PayName = '"
                        + strPayName + "', PayId = '" + strPayId + "', PayType = '" + strPayType + "', PayScope = '"
                        + strPayScope + "', PrepareBank = '" + strPrepareBank + "', CountDate = " + nCountDate + ", State = "
                        + nState + ", Flag = " + nFlag + ", UpdateTime = '" + strUpdateTime + "' where PayId = '" + strPayId + "' and CompanyId = '"+ strCompanyId + "'";
            }
            else if(nFlag == 3)
            {
                strSql = "delete from tb_companypay where PayId = '"+ strPayId + "' and CompanyId = '"+ strCompanyId + "'";
            }

            //
            jdbcTemplate.batchUpdate(strSql);
        }
        else if(strInfoFlag.equals("baseInfoCompanyService"))
        {
            String strCompanyId = obj.getString("CompanyId");
            int nAddress = obj.getIntValue("Address");
            String strServiceName = obj.getString("ServiceName");
            String strServiceNo = obj.getString("ServiceNo");
            String strDetailAddress = obj.getString("DetailAddress");
            String strResponsibleName = obj.getString("ResponsibleName");
            String strResponsiblePhone = obj.getString("ResponsiblePhone");
            String strManagerName = obj.getString("ManagerName");
            String strManagerPhone = obj.getString("ManagerPhone");
            String strContactPhone = "";
            if(obj.containsKey("ContactPhone"))
            {
                strContactPhone = obj.getString("ContactPhone");
            }
            String strMailAddress = obj.getString("MailAddress");
            int nCreateDate = obj.getIntValue("CreateDate");
            int nState = obj.getIntValue("State");
            int nFlag = obj.getIntValue("Flag");
            String strUpdateTime = obj.getLong("UpdateTime").toString();
            if(nFlag == 1)
            {
                strSql = "insert into tb_companyservice(CompanyId, Address, ServiceName, ServiceNo, DetailAddress, ResponsibleName, ResponsiblePhone,"
                        + "ManagerName, ManagerPhone, ContactPhone, MailAddress, CreateDate, State, Flag, UpdateTime) values ('"
                        + strCompanyId + "', "
                        + nAddress + ", '"
                        + strServiceName + "', '"
                        + strServiceNo + "', '"
                        + strDetailAddress + "', '"
                        + strResponsibleName + "', '"
                        + strResponsiblePhone + "', '"
                        + strManagerName + "', '"
                        + strManagerPhone + "', '"
                        + strContactPhone + "', '"
                        + strMailAddress + "', "
                        + nCreateDate + ", "
                        + nState + ", "
                        + nFlag + ", '"
                        + strUpdateTime + "')";
            }
            else if(nFlag == 2)
            {
                strSql = "update tb_companyservice set CompanyId = '" + strCompanyId + "', Address = "
                        + nAddress + ", ServiceName = '" + strServiceName +  "', ServiceNo = '"
                        + strServiceNo + "', DetailAddress = '" + strDetailAddress + "', ResponsibleName = '"
                        + strResponsibleName + "', ResponsiblePhone = '" + strResponsiblePhone + "', ManagerName = '"
                        + strManagerName + "', ManagerPhone = '" + strManagerPhone + "', ContactPhone = '"
                        + strContactPhone + "', MailAddress = '" + strMailAddress + "', CreateDate = "
                        + nCreateDate + ", State = " + nState + ", Flag = " + nFlag + ", UpdateTime = '" + strUpdateTime + "' where ServiceNo = '"
                        + strServiceNo + "' and CompanyId = '"+ strCompanyId + "'";
            }
            else if(nFlag == 3)
            {
                strSql = "delete from tb_companyservice where ServiceNo = '"+ strServiceNo + "' and CompanyId = '"+ strCompanyId + "'";
            }

            //
            jdbcTemplate.batchUpdate(strSql);
        }
        else if(strInfoFlag.equals("baseInfoCompanyPermit"))
        {
            String strCompanyId = obj.getString("CompanyId");
            int nAddress = obj.getIntValue("Address");
            String strCertificate = obj.getString("Certificate");
            String strOperationArea = obj.getString("OperationArea");
            String strOwnerName = obj.getString("OwnerName");
            String strOrganization = obj.getString("Organization");
            String strStartDate = obj.getInteger("StartDate").toString();
            String strStopDate = obj.getInteger("StopDate").toString();
            String strCertifyDate = obj.getInteger("CertifyDate").toString();
            String strState = obj.getString("State");
            int nFlag = obj.getIntValue("Flag");
            String strUpdateTime = obj.getLong("UpdateTime").toString();
            if(nFlag == 1)
            {
                strSql = "insert into tb_companypermit(CompanyId, Address, Certificate, OperationArea, OwnerName, Organization, StartDate, StopDate, CertifyDate, "
                        + "State, Flag, UpdateTime) values ('"
                        + strCompanyId + "', "
                        + nAddress + ", '"
                        + strCertificate + "', '"
                        + strOperationArea + "', '"
                        + strOwnerName + "', '"
                        + strOrganization + "', '"
                        + strStartDate + "', '"
                        + strStopDate + "', '"
                        + strCertifyDate + "', '"
                        + strState + "', "
                        + nFlag + ", '"
                        + strUpdateTime + "')";

            }
            else if(nFlag == 2)
            {
                strSql = "update tb_companypermit set CompanyId = '" + strCompanyId + "', Address = "
                        + nAddress + ", Certificate = '" + strCertificate + "', OperationArea = '"
                        + strOperationArea + "', OwnerName = '" + strOwnerName + "', Organization = '"
                        + strOrganization + "', StartDate = '" + strStartDate + "', StopDate = '"
                        + strStopDate + "', CertifyDate = '" + strCertifyDate + "', State = '"
                        + strState + "', Flag = " + nFlag + ", UpdateTime = '" + strUpdateTime + "' where CompanyId = '"+ strCompanyId + "'";
            }
            else if(nFlag == 3)
            {
                strSql = "delete from tb_companypermit where Certificate = '" + strCertificate + "' and CompanyId = '"+ strCompanyId + "'";
            }
            //
            jdbcTemplate.batchUpdate(strSql);
        }
        else if(strInfoFlag.equals("baseInfoCompanyFare"))
        {
            String strCompanyId = obj.getString("CompanyId");
            int nAddress = obj.getIntValue("Address");
            String strFareType = obj.getString("FareType");
            String strFareTypeNote = "";
            if(obj.containsKey("FareTypeNote")) strFareTypeNote = obj.getString("FareTypeNote");
            String strFareValidOn = "";
            if(obj.containsKey("strFareValidOn")) strFareValidOn = obj.getLong("FareValidOn").toString();
            String strFareValidOff = "";
            if(obj.containsKey("strFareValidOff")) strFareValidOff = obj.getLong("FareValidOff").toString();

            double dStartFare = obj.getDoubleValue("StartFare");
            double dStartMile = obj.getDoubleValue("StartMile");
            double dUnitPricePerMile = obj.getDoubleValue("UnitPricePerMile");
            double dUnitPricePerMinute = obj.getDoubleValue("UnitPricePerMinute");
            double dUpPrice = 0;
            if(obj.containsKey("UpPrice")) dUpPrice = obj.getDoubleValue("UpPrice");

            double dUpPriceStartMile = 0;
            if(obj.containsKey("UpPriceStartMile"))	dUpPriceStartMile = obj.getDoubleValue("UpPriceStartMile");

            String strMorningPeakTimeOn = obj.getString("MorningPeakTimeOn");
            String strMorningPeakTimeOff = obj.getString("MorningPeakTimeOff");
            String strEveningPeakTimeOn = obj.getString("EveningPeakTimeOn");
            String strEveningPeakTimeOff = obj.getString("EveningPeakTimeOff");

            String strOtherPeakTimeOn = "";
            if(obj.containsKey("OtherPeakTimeOn"))
            {
                strOtherPeakTimeOn = obj.getString("OtherPeakTimeOn");
            }

            String strOtherPeakTimeOff = "";
            if(obj.containsKey("OtherPeakTimeOff"))
            {
                strOtherPeakTimeOff = obj.getString("OtherPeakTimeOff");
            }

            double dPeakUnitPrice = obj.getDoubleValue("PeakUnitPrice");
            double dPeakPriceStartMile = obj.getDoubleValue("PeakPriceStartMile");

            double dLowSpeedPricePerMinute = 0;
            if(obj.containsKey("LowSpeedPricePerMinute"))
            {
                dLowSpeedPricePerMinute = obj.getDoubleValue("LowSpeedPricePerMinute");
            }

            double dNightPricePerMile = 0;
            if(obj.containsKey("NightPricePerMile"))
            {
                dNightPricePerMile = obj.getDoubleValue("NightPricePerMile");
            }

            double dNightPricePerMinute = 0;
            if(obj.containsKey("NightPricePerMinute"))
            {
                dNightPricePerMinute = obj.getDoubleValue("NightPricePerMinute");
            }

            double dOtherPrice = 0;
            if(obj.containsKey("OtherPrice"))
            {
                dOtherPrice = obj.getDoubleValue("OtherPrice");
            }

            int nState = obj.getIntValue("State");
            int nFlag = obj.getIntValue("Flag");
            String strUpdateTime = obj.getLong("UpdateTime").toString();
            if(nFlag == 1)
            {
                strSql = "insert into tb_companyfare(CompanyId, Address, FareType, FareTypeNote, FareValidOn, FareValidOff, StartFare, StartMile, UnitPricePerMile, "
                        + "UnitPricePerMinute, UpPrice, UpPriceStartMile, MorningPeakTimeOn, MorningPeakTimeOff, EveningPeakTimeOn, EveningPeakTimeOff, OtherPeakTimeOn, "
                        + "OtherPeakTimeOff, PeakUnitPrice, PeakPriceStartMile, LowSpeedPricePerMinute, NightPricePerMile,"
                        + "NightPricePerMinute, OtherPrice, State, Flag, UpdateTime) values ('"
                        + strCompanyId + "', "
                        + nAddress + ", '"
                        + strFareType + "', '"
                        + strFareTypeNote + "', '"
                        + strFareValidOn + "', '"
                        + strFareValidOff + "', "
                        + dStartFare + ", "
                        + dStartMile + ", "
                        + dUnitPricePerMile + ", "
                        + dUnitPricePerMinute + ", "
                        + dUpPrice + ", "
                        + dUpPriceStartMile + ", '"
                        + strMorningPeakTimeOn + "', '"
                        + strMorningPeakTimeOff + "', '"
                        + strEveningPeakTimeOn + "', '"
                        + strEveningPeakTimeOff + "', '"
                        + strOtherPeakTimeOn + "', '"
                        + strOtherPeakTimeOff + "', "
                        + dPeakUnitPrice + ", "
                        + dPeakPriceStartMile + ", "
                        + dLowSpeedPricePerMinute + ", "
                        + dNightPricePerMile + ", "
                        + dNightPricePerMinute + ", "
                        + dOtherPrice + ", "
                        + nState + ", "
                        + nFlag + ", '"
                        + strUpdateTime + "')";
            }
            else if(nFlag == 2)
            {
                strSql = "update tb_companyfare set CompanyId = '" + strCompanyId + "', Address = "
                        + nAddress + ", FareType = '" + strFareType + "', FareTypeNote = '"
                        + strFareTypeNote + "', FareValidOn = '" + strFareValidOn + "', FareValidOff = '"
                        + strFareValidOff + "', StartFare = " + dStartFare + ", StartMile = "
                        + dStartMile + ", UnitPricePerMile = " + dUnitPricePerMile + ", UnitPricePerMinute = "
                        + dUnitPricePerMinute + ", UpPrice = " + dUpPrice + ", UpPriceStartMile = " + dUpPriceStartMile + ", MorningPeakTimeOn = '"
                        + strMorningPeakTimeOn + "', MorningPeakTimeOff = '" + strMorningPeakTimeOff + "', EveningPeakTimeOn = '"
                        + strEveningPeakTimeOn + "', EveningPeakTimeOff = '" + strEveningPeakTimeOff + "', OtherPeakTimeOn = '"
                        + strOtherPeakTimeOn + "', OtherPeakTimeOff = '" + strOtherPeakTimeOff + "', PeakUnitPrice = "
                        + dPeakUnitPrice + ", PeakPriceStartMile = " + dPeakPriceStartMile + ", LowSpeedPricePerMinute = "
                        + dLowSpeedPricePerMinute + ", NightPricePerMile = " + dNightPricePerMile + ", NightPricePerMinute = "
                        + dNightPricePerMinute + ", OtherPrice = " + dOtherPrice + ", State = " + nState + ", Flag = " + nFlag + ", UpdateTime = '"
                        + strUpdateTime + "' where FareType = '" + strFareType + "' and CompanyId = '"+ strCompanyId + "'";
            }
            else if(nFlag == 3)
            {
                strSql = "delete from tb_companyfare where FareType = '" + strFareType + "' and CompanyId = '"+ strCompanyId + "'";
            }

            //
            jdbcTemplate.batchUpdate(strSql);
        }
        else if(strInfoFlag.equals("baseInfoVehicle"))
        {
            String strCompanyId = obj.getString("CompanyId");
            int nAddress = obj.getIntValue("Address");
            String strVehicleNo = obj.getString("VehicleNo");
            String strPlateColor = obj.getString("PlateColor");
            int nSeats = obj.getIntValue("Seats");
            String strBrand = obj.getString("Brand");
            String strModel = obj.getString("Model");
            String strVehicleType = obj.getString("VehicleType");
            String strOwnerName = obj.getString("OwnerName");
            String strVehicleColor = obj.getString("VehicleColor");
            String strEngineId = obj.getString("EngineId");
            String strVIN = obj.getString("VIN");
            String strCertifyDateA = obj.getLong("CertifyDateA").toString();
            String strFuelType = obj.getString("FuelType");
            String strEngineDisplace = obj.getString("EngineDisplace");
            String strPhotoId = "";
            if(obj.containsKey("PhotoId"))
            {
                strPhotoId = obj.getString("PhotoId");
            }

            String strCertificate = "";
            if(obj.containsKey("Certificate"))
            {
                strCertificate = obj.getString("Certificate");
            }

            String strTransAgency = obj.getString("TransAgency");
            String strTransArea = obj.getString("TransArea");

            String strTransDateStart = obj.getLong("TransDateStart").toString();
            String strTransDateStop = obj.getLong("TransDateStop").toString();
            String strCertifyDateB = obj.getLong("CertifyDateB").toString();
            String strFixState = obj.getString("FixState");
            long nNextFixDate = 0;
            if(obj.containsKey("NextFixDate"))
            {
                nNextFixDate = obj.getLongValue("NextFixDate");
            }

            String strCheckState = obj.getString("CheckState");
            String strFeePrintId = obj.getString("FeePrintId");
            String strGPSBrand = obj.getString("GPSBrand");
            String strGPSModel = obj.getString("GPSModel");

            String strGPSIMEI = "";
            if(obj.containsKey("GPSIMEI"))
            {
                strGPSIMEI = obj.getString("GPSIMEI");
            }

            String strGPSInstallDate = obj.getLong("GPSInstallDate").toString();
            String strRegisterDate = obj.getLong("RegisterDate").toString();
            int nCommercialType = obj.getIntValue("CommercialType");
            String strFareType = obj.getString("FareType");
            int nState = obj.getIntValue("State");
            int nFlag = obj.getIntValue("Flag");
            if(nFlag == 0) nFlag = 1;
            String strUpdateTime = obj.getLong("UpdateTime").toString();
            if(nFlag == 1)
            {
                strSql = "insert into tb_vehiclebaseinfo(CompanyId, Address, VehicleNo, PlateColor, Seats, Brand, Model, VehicleType,"
                        + " OwnerName, VehicleColor, EngineId, VIN, CertifyDateA, FuelType, EngineDisplace, PhotoId, Certificate"
                        + ", TransAgency, TransArea, TransDateStart, TransDateStop, CertifyDateB, FixState, NextFixDate, CheckState, "
                        + "FeePrintId, GPSBrand, GPSModel, GPSIMEI, GPSInstallDate, RegisterDate, CommercialType, FareType, State, Flag, UpdateTime) values ('"
                        + strCompanyId + "', "
                        + nAddress + ", '"
                        + strVehicleNo + "', '"
                        + strPlateColor + "', "
                        + nSeats + ", '"
                        + strBrand + "', '"
                        + strModel + "', '"
                        + strVehicleType + "', '"
                        + strOwnerName + "', '"
                        + strVehicleColor + "', '"
                        + strEngineId + "', '"
                        + strVIN + "', '"
                        + strCertifyDateA + "', '"
                        + strFuelType + "', '"
                        + strEngineDisplace + "', '"
                        + strPhotoId + "', '"
                        + strCertificate + "', '"
                        + strTransAgency + "', '"
                        + strTransArea + "', '"
                        + strTransDateStart + "', '"
                        + strTransDateStop + "', '"
                        + strCertifyDateB + "', '"
                        + strFixState + "', "
                        + nNextFixDate + ", '"
                        + strCheckState + "', '"
                        + strFeePrintId + "', '"
                        + strGPSBrand + "', '"
                        + strGPSModel + "', '"
                        + strGPSIMEI + "', '"
                        + strGPSInstallDate + "', '"
                        + strRegisterDate + "', "
                        + nCommercialType + ", '"
                        + strFareType + "', "
                        + nState + ", "
                        + nFlag + ", '"
                        + strUpdateTime + "')";
            }
            else if(nFlag == 2)
            {
                strSql = "update tb_vehiclebaseinfo set CompanyId = '" + strCompanyId + "', Address = " + nAddress + ", VehicleNo = '" + strVehicleNo + "', PlateColor = '"
                        + strPlateColor + "', Seats = " + nSeats + ", Brand = '" + strBrand + "', Model = '" + strModel + "', VehicleType = '" + strVehicleType + "', "
                        + "OwnerName = '" + strOwnerName + "', VehicleColor = '" + strVehicleColor + "', EngineId = '" + strEngineId + "', VIN = '" + strVIN + "', "
                        + "CertifyDateA = '" + strCertifyDateA + "', FuelType = '" + strFuelType + "', EngineDisplace = '" + strEngineDisplace + "', PhotoId = '"
                        + strPhotoId + "', Certificate = '" + strCertificate + "', TransAgency = '" + strTransAgency + "', TransArea = '" + strTransArea + "', TransDateStart = '"
                        + strTransDateStart + "', TransDateStop = '" + strTransDateStop + "', CertifyDateB = '" + strCertifyDateB + "', FixState = '" + strFixState + "', NextFixDate = "
                        + nNextFixDate + ", CheckState = '" + strCheckState + "', FeePrintId = '" + strFeePrintId + "', GPSBrand = '" + strGPSBrand + "', GPSModel = '"
                        + strGPSModel + "', GPSIMEI = '" + strGPSIMEI + "', GPSInstallDate = '" + strGPSInstallDate + "', RegisterDate = '" + strRegisterDate + "', CommercialType = "
                        + nCommercialType + ", FareType = '" + strFareType + "', State = " + nState + ", Flag = " + nFlag + ", UpdateTime = '" + strUpdateTime + "' where VehicleNo = '"
                        + strVehicleNo + "' and CompanyId = '"+ strCompanyId + "'";
            }
            else if(nFlag == 3)
            {
                strSql = "delete from tb_vehiclebaseinfo where VehicleNo = '" + strVehicleNo + "' and CompanyId = '"+ strCompanyId + "'";
            }

            //
            jdbcTemplate.batchUpdate(strSql);
        }
        else if(strInfoFlag.equals("baseInfoVehicleInsurance"))
        {
            String strCompanyId = obj.getString("CompanyId");
            String strVehicleNo = obj.getString("VehicleNo");
            String strInsurCom = obj.getString("InsurCom");
            String strInsurNum = obj.getString("InsurNum");
            String strInsurType = obj.getString("InsurType");
            double dInsurCount = obj.getDoubleValue("InsurCount");
            String strInsurEff = obj.getLong("InsurEff").toString();
            String strInsurExp = obj.getLong("InsurExp").toString();
            int nFlag = obj.getIntValue("Flag");
            String strUpdateTime = obj.getLong("UpdateTime").toString();
            if(nFlag == 1)
            {
                strSql = "insert into tb_vehicleinsurance(CompanyId, VehicleNo, InsurCom, InsurNum, InsurType, InsurCount, InsurEff, InsurExp, Flag, UpdateTime) values ('"
                        + strCompanyId + "', '"
                        + strVehicleNo + "', '"
                        + strInsurCom + "', '"
                        + strInsurNum + "', '"
                        + strInsurType + "', "
                        + dInsurCount + ", '"
                        + strInsurEff + "', '"
                        + strInsurExp + "', "
                        + nFlag + ", '"
                        + strUpdateTime + "')";
            }
            else if(nFlag == 2)
            {
                strSql = "update tb_vehicleinsurance set CompanyId = '" + strCompanyId + "', VehicleNo = '" + strVehicleNo + "', InsurCom = '"
                        + strInsurCom + "', InsurNum = '" + strInsurNum + "', InsurType = '" + strInsurType + "', InsurCount = "
                        + dInsurCount + ", InsurEff = '" + strInsurEff + "', InsurExp = '" + strInsurExp + "', Flag = " + nFlag + ", UpdateTime = '"
                        + strUpdateTime + "' where VehicleNo = '" + strVehicleNo + "' and CompanyId = '"+ strCompanyId + "'";
            }
            else if(nFlag == 3)
            {
                strSql = "delete from tb_vehicleinsurance where VehicleNo = '" + strVehicleNo + "' and CompanyId = '"+ strCompanyId + "'";
            }

            //
            jdbcTemplate.batchUpdate(strSql);
        }
        else if(strInfoFlag.equals("baseInfoVehicleTotalMile"))
        {
            String strCompanyId = obj.getString("CompanyId");
            int nAddress = obj.getIntValue("Address");
            String strVehicleNo = obj.getString("VehicleNo");
            double dTotalMile = obj.getDoubleValue("TotalMile");
            int nFlag = obj.getIntValue("Flag");
            String strUpdateTime = obj.getLong("UpdateTime").toString();
            if(nFlag == 1)
            {
                strSql = "insert into tb_vehicletotalmile(CompanyId, Address, VehicleNo, TotalMile, Flag, UpdateTime) values ('"
                        + strCompanyId + "', "
                        + nAddress + ", '"
                        + strVehicleNo + "', "
                        + dTotalMile + ", "
                        + nFlag + ", '"
                        + strUpdateTime + "')";

            }
            else if(nFlag == 2)
            {
                strSql = "update tb_vehicletotalmile set CompanyId = '" + strCompanyId + "', Address = " + nAddress + ", VehicleNo = '"
                        + strVehicleNo + "', TotalMile = " + dTotalMile + ", Flag = " + nFlag + ", UpdateTime = '" + strUpdateTime + "' where VehicleNo = '"
                        + strVehicleNo + "' and CompanyId = '"+ strCompanyId + "'";
            }
            else if(nFlag == 3)
            {
                strSql = "delete from tb_vehicletotalmile where VehicleNo = '" + strVehicleNo + "' and CompanyId = '"+ strCompanyId + "'";
            }

            //
            jdbcTemplate.batchUpdate(strSql);
        }
        else if(strInfoFlag.equals("baseInfoDriver"))
        {
            LOG.info("得到驾驶员基本信息：" + obj.toJSONString());
            String strCompanyId = obj.getString("CompanyId");
            int nAddress = obj.getIntValue("Address");//
            String strDriverName = obj.getString("DriverName");//
            String strDriverPhone = obj.getString("DriverPhone");//
            String strDriverGender = obj.getString("DriverGender");
            String strDriverBirthday = obj.getLong("DriverBirthday").toString();
            String strDriverNationality = "";
            if(obj.containsKey("DriverNationality"))
            {
                strDriverNationality = obj.getString("DriverNationality");//
            }

            String strDriverNation = obj.getString("DriverNation");//
            String strDriverMaritalStatus = "";
            if(obj.containsKey("DriverMaritalStatus"))
            {
                strDriverMaritalStatus = obj.getString("DriverMaritalStatus");
            }
            String strDriverLanguageLevel = "";
            if(obj.containsKey("DriverLanguageLevel"))
            {
                strDriverLanguageLevel = obj.getString("DriverLanguageLevel");//
            }
            String strDriverEducation = "";
            if(obj.containsKey("DriverEducation"))
            {
                strDriverEducation = obj.getString("DriverEducation");
            }
            String strDriverCensus = "";
            if(obj.containsKey("DriverCensus"))
            {
                strDriverCensus = obj.getString("DriverCensus");//
            }
            String strDriverAddress = "";
            if(obj.containsKey("DriverAddress"))
            {
                strDriverAddress = obj.getString("DriverAddress");
            }
            String strDriverContactAddress = obj.getString("DriverContactAddress");

            String strPhotoId = "";
            if(obj.containsKey("PhotoId"))
            {
                strPhotoId = obj.getString("PhotoId");
            }

            String strLicenseId = obj.getString("LicenseId");

            String strLicensePhotoId = "";
            if(obj.containsKey("LicensePhotoId"))
            {
                strLicensePhotoId = obj.getString("LicensePhotoId");
            }

            String strDirverType = "";
            if(obj.containsKey("DriverType"))
            {
                strDirverType = obj.getString("DriverType");//
            }

            String strGetDriverLicenseDate = obj.getLong("GetDriverLicenseDate").toString();
            String strDriverLicenseOn = obj.getLong("DriverLicenseOn").toString();
            String strDriverLicenseOff = obj.getLong("DriverLicenseOff").toString();//
            int nTaxiDriver = obj.getIntValue("TaxiDriver");//
            String strCertificateNo = obj.getString("CertificateNo");
            String strNetworkCarIssueOrganization = obj.getString("NetworkCarIssueOrganization");
            String strNetworkCarIssueDate = obj.getLong("NetworkCarIssueDate").toString();
            String strGetNetworkCarProofDate = obj.getLong("GetNetworkCarProofDate").toString();
            String strNetworkCarProofOn = obj.getLong("NetworkCarProofOn").toString();
            String strNetworkCarProofOff = obj.getLong("NetworkCarProofOff").toString();//
            String strRegisterDate = obj.getLong("RegisterDate").toString();
            int nFullTimeDriver = 0;
            if(obj.containsKey("FullTimeDriver"))
            {
                nFullTimeDriver = obj.getIntValue("FullTimeDriver");
            }

            int nInDriverBlacklist = 0;
            if(obj.containsKey("InDriverBlacklist"))
            {
                nInDriverBlacklist = obj.getIntValue("InDriverBlacklist");
            }

            int nCommercialType = obj.getIntValue("CommercialType");
            String strContractCompany = obj.getString("ContractCompany");//
            String strContractOn = obj.getLong("ContractOn").toString();
            String strContractOff = obj.getLong("ContractOff").toString();//
            String strEmergencyContact = "";//
            if(obj.containsKey("EmergencyContact"))
            {
                strEmergencyContact = obj.getString("EmergencyContact");
            }
            String strEmergencyContactPhone = "";
            if(obj.containsKey("EmergencyContactPhone"))
            {
                strEmergencyContactPhone = obj.getString("EmergencyContactPhone");
            }
            String strEmergencyContactAddress = "";
            if(obj.containsKey("EmergencyContactAddress"))
            {
                strEmergencyContactAddress = obj.getString("EmergencyContactAddress");
            }
            int nState = obj.getIntValue("State");
            int nFlag = obj.getIntValue("Flag");
            String strUpdateTime = obj.getLong("UpdateTime").toString();
            if(nFlag == 1)
            {
                strSql = "insert into tb_driverbaseinfo(CompanyId, Address, DriverName, DriverPhone, DriverGender, DriverBirthday, "
                        + "DriverNationality, DriverNation, DriverMaritalStatus, DriverLanguageLevel, DriverEducation, DriverCensus, "
                        + "DriverAddress, DriverContactAddress, PhotoId, LicenseId, LicensePhotoId, DriverType, GetDriverLicenseDate, "
                        + "DriverLicenseOn, DriverLicenseOff, TaxiDriver, CertificateNo, NetworkCarIssueOrganization, NetworkCarIssueDate, "
                        + "GetNetworkCarProofDate, NetworkCarProofOn, NetworkCarProofOff, RegisterDate, FullTimeDriver, InDriverBlacklist, "
                        + "CommercialType, ContractCompany,  ContractOn, ContractOff, EmergencyContact, EmergencyContactPhone, EmergencyContactAddress, "
                        + "State, Flag, UpdateTime) values ('"
                        + strCompanyId + "', "
                        + nAddress + ", '"
                        + strDriverName + "', '"
                        + strDriverPhone + "', '"
                        + strDriverGender + "', '"
                        + strDriverBirthday + "', '"
                        + strDriverNationality + "', '"
                        + strDriverNation + "', '"
                        + strDriverMaritalStatus + "', '"
                        + strDriverLanguageLevel + "', '"
                        + strDriverEducation + "', '"
                        + strDriverCensus + "', '"
                        + strDriverAddress + "', '"
                        + strDriverContactAddress + "', '"
                        + strPhotoId + "', '"
                        + strLicenseId + "', '"
                        + strLicensePhotoId + "', '"
                        + strDirverType + "', '"
                        + strGetDriverLicenseDate + "', '"
                        + strDriverLicenseOn + "', '"
                        + strDriverLicenseOff + "', "
                        + nTaxiDriver + ", '"
                        + strCertificateNo + "', '"
                        + strNetworkCarIssueOrganization + "', '"
                        + strNetworkCarIssueDate + "', '"
                        + strGetNetworkCarProofDate + "', '"
                        + strNetworkCarProofOn + "', '"
                        + strNetworkCarProofOff + "', '"
                        + strRegisterDate + "', "
                        + nFullTimeDriver + ", "
                        + nInDriverBlacklist + ", "
                        + nCommercialType + ", '"
                        + strContractCompany + "', '"
                        + strContractOn + "', '"
                        + strContractOff + "', '"
                        + strEmergencyContact + "', '"
                        + strEmergencyContactPhone + "', '"
                        + strEmergencyContactAddress + "', "
                        + nState + ", "
                        + nFlag + ", '"
                        + strUpdateTime + "')";
            }
            else if(nFlag == 2)
            {
                strSql = "update tb_driverbaseinfo set CompanyId = '" + strCompanyId + "', Address = " + nAddress + ", DriverName = '"
                        + strDriverName + "', DriverPhone = '" + strDriverPhone + "', DriverGender = '" + strDriverGender + "', DriverBirthday = '"
                        + strDriverBirthday + "', DriverNationality = '" + strDriverNationality + "', DriverNation = '" + strDriverNation + "', DriverMaritalStatus = '"
                        + strDriverMaritalStatus + "', DriverLanguageLevel = '" + strDriverLanguageLevel + "', DriverEducation = '" + strDriverEducation + "', DriverCensus = '"
                        + strDriverCensus + "', DriverAddress = '" + strDriverAddress + "', DriverContactAddress = '" + strDriverContactAddress + "', PhotoId = '"
                        + strPhotoId + "', LicenseId = '" + strLicenseId + "', LicensePhotoId = '" + strLicensePhotoId + "', DriverType = '" + strDirverType + "', GetDriverLicenseDate = '"
                        + strGetDriverLicenseDate + "', DriverLicenseOn = '" + strDriverLicenseOn + "', DriverLicenseOff = '" + strDriverLicenseOff + "', TaxiDriver = "
                        + nTaxiDriver + ", CertificateNo = '" + strCertificateNo + "', NetworkCarIssueOrganization = '" + strNetworkCarIssueOrganization + "', NetworkCarIssueDate = '"
                        + strNetworkCarIssueDate + "', GetNetworkCarProofDate = '" + strGetNetworkCarProofDate + "', NetworkCarProofOn = '" + strNetworkCarProofOn + "', NetworkCarProofOff = '"
                        + strNetworkCarProofOff + "', RegisterDate = '" + strRegisterDate + "', FullTimeDriver = " + nFullTimeDriver + ", InDriverBlacklist = "
                        + nInDriverBlacklist + ", CommercialType = " + nCommercialType + ", ContractCompany = '" + strContractCompany + "', ContractOn = '"
                        + strContractOn + "', ContractOff = '" + strContractOff + "', EmergencyContact = '" + strEmergencyContact + "', EmergencyContactPhone = '"
                        + strEmergencyContactPhone + "', EmergencyContactAddress = '" + strEmergencyContactAddress + "', State = " + nState + ", Flag = " + nFlag + ", UpdateTime = '"
                        + strUpdateTime + "' where LicenseId = '" + strLicenseId + "' and CompanyId = '"+ strCompanyId + "'";
            }
            else if(nFlag == 3)
            {
                strSql = "delete from tb_driverbaseinfo where LicenseId = '" + strLicenseId + "' and CompanyId = '"+ strCompanyId + "'";
            }
            //
            jdbcTemplate.batchUpdate(strSql);
        }
        else if(strInfoFlag.equals("baseInfoDriverEducate"))
        {
            String strCompanyId = obj.getString("CompanyId");
            int nAddress = obj.getIntValue("Address");
            String strLicenseId = obj.getString("LicenseId");
            String strCourseName = obj.getString("CourseName");
            String strCourseDate = obj.getLong("CourseDate").toString();
            String strStartTime = obj.getString("StartTime");
            String strStopTime = obj.getString("StopTime");
            int nDuration = obj.getIntValue("Duration");
            int nFlag = obj.getIntValue("Flag");
            String strUpdateTime = obj.getLong("UpdateTime").toString();
            if(nFlag == 1)
            {
                strSql = "insert into tb_drivereducate(CompanyId, Address, LicenseId, CourseName, CourseDate, StartTime, StopTime, Duration, "
                        + "Flag, UpdateTime) values ('"
                        + strCompanyId + "', "
                        + nAddress + ", '"
                        + strLicenseId + "', '"
                        + strCourseName + "', '"
                        + strCourseDate + "', '"
                        + strStartTime + "', '"
                        + strStopTime + "', "
                        + nDuration + ", "
                        + nFlag + ", '"
                        + strUpdateTime + "')";
            }
            else if(nFlag == 2)
            {
                strSql = "update tb_drivereducate set CompanyId = '" + strCompanyId + "', Address = " + nAddress + ", LicenseId = '" + strLicenseId + "', CourseName = '"
                        + strCourseName + "', CourseDate = '" + strCourseDate + "', StartTime = '" + strStartTime + "', StopTime = '" + strStopTime + "', Duration = "
                        + nDuration + ", Flag = " + nFlag + ", UpdateTime = '" + strUpdateTime + "' where LicenseId = '" + strLicenseId + "' and CompanyId = '"+ strCompanyId + "'";
            }
            else if(nFlag == 3)
            {
                strSql = "delete from tb_drivereducate where LicenseId = '" + strLicenseId + "' and CompanyId = '"+ strCompanyId + "'";
            }

            //
            jdbcTemplate.batchUpdate(strSql);
        }
        else if(strInfoFlag.equals("baseInfoDriverApp"))
        {
            String strCompanyId = obj.getString("CompanyId");
            int nAddress = obj.getIntValue("Address");
            String strLicenseId = obj.getString("LicenseId");
            String strDriverPhone = obj.getString("DriverPhone");
            int nNetType = obj.getIntValue("NetType");
            String strAppVersion = obj.getString("AppVersion");
            int nMapType = obj.getIntValue("MapType");
            int nState = obj.getIntValue("State");
            int nFlag = obj.getIntValue("Flag");
            String strUpdateTime = obj.getLong("UpdateTime").toString();
            if(nFlag == 1)
            {
                strSql = "insert into tb_driverapp(CompanyId, Address, LicenseId, DriverPhone, NetType, AppVersion, MapType, State, "
                        + "Flag, UpdateTime) values ('"
                        + strCompanyId + "', "
                        + nAddress + ", '"
                        + strLicenseId + "', '"
                        + strDriverPhone + "', "
                        + nNetType + ", '"
                        + strAppVersion + "', "
                        + nMapType + ", "
                        + nState + ", "
                        + nFlag + ", '"
                        + strUpdateTime + "')";
            }
            else if(nFlag == 2)
            {
                strSql = "update tb_driverapp set CompanyId = '" + strCompanyId + "', Address = " + nAddress + ", LicenseId = '"
                        + strLicenseId + "', DriverPhone = '" + strDriverPhone + "', NetType = " + nNetType + ", AppVersion = '"
                        + strAppVersion + "', MapType = " + nMapType + ", State = " + nState + ", Flag = " + nFlag + ", UpdateTime = '"
                        + strUpdateTime + "' where LicenseId = '" + strLicenseId + "' and CompanyId = '"+ strCompanyId + "'";
            }
            else if(nFlag == 3)
            {
                strSql = "delete from tb_driverapp where LicenseId = '" + strLicenseId + "' and CompanyId = '"+ strCompanyId + "'";
            }

            //
            jdbcTemplate.batchUpdate(strSql);
        }
        else if(strInfoFlag.equals("baseInfoDriverStat"))
        {
            String strCompanyId = obj.getString("CompanyId");
            int nAddress = obj.getIntValue("Address");
            String strLicenseId = obj.getString("LicenseId");
            int nCycle = obj.getIntValue("Cycle");
            int nOrderCount = obj.getIntValue("OrderCount");
            int nTrafficViolationCount = obj.getIntValue("TrafficViolationCount");
            int nComplainedCount = obj.getIntValue("ComplainedCount");
            int nFlag = obj.getIntValue("Flag");
            String strUpdateTime = obj.getLong("UpdateTime").toString();
            if(nFlag == 1)
            {
                strSql = "insert into tb_driverstat(CompanyId, Address, LicenseId, Cycle, OrderCount, TrafficViolationCount, ComplainedCount, Flag, UpdateTime) values ('"
                        + strCompanyId + "', "
                        + nAddress + ", '"
                        + strLicenseId + "', "
                        + nCycle + ", "
                        + nOrderCount + ", "
                        + nTrafficViolationCount + ", "
                        + nComplainedCount + ", "
                        + nFlag + ", '"
                        + strUpdateTime + "')";
            }
            else if(nFlag == 2)
            {
                strSql = "update tb_driverstat set CompanyId = '" + strCompanyId + "', Address = " + nAddress + ", LicenseId = '"
                        + strLicenseId + "', Cycle = " + nCycle + ", OrderCount = " + nOrderCount + ", TrafficViolationCount = "
                        + nTrafficViolationCount + ", ComplainedCount = " + nComplainedCount + ", Flag = " + nFlag + ", UpdateTime = '"
                        + strUpdateTime + "' where LicenseId = '" + strLicenseId + "' and CompanyId = '"+ strCompanyId + "'";
            }
            else if(nFlag == 3)
            {
                strSql = "delete from tb_driverstat where LicenseId = '" + strLicenseId + "' and CompanyId = '"+ strCompanyId + "'";
            }
            //
            jdbcTemplate.batchUpdate(strSql);
        }
        else if(strInfoFlag.equals("baseInfoPassenger"))
        {
            String strCompanyId = obj.getString("CompanyId");
            String strRegisterDate = "";
            if(obj.containsKey("RegisterDate"))
            {
                strRegisterDate = obj.getLong("RegisterDate").toString();
            }

            String strPassengerPhone = obj.getString("PassengerPhone");
            String strPassengerName = "";
            if(obj.containsKey("PassengerName"))
            {
                strPassengerName = obj.getString("PassengerName");
            }

            String strPassengerGender = "";
            if(obj.containsKey("PassengerGender"))
            {
                strPassengerGender = obj.getString("PassengerGender");
            }
            int nState = obj.getIntValue("State");
            int nFlag = obj.getIntValue("Flag");
            String strUpdateTime = obj.getLong("UpdateTime").toString();
            if(nFlag == 1)
            {
                strSql = "insert into tb_passengerbaseinfo(CompanyId, RegisterDate, PassengerPhone, PassengerName, PassengerGender, "
                        + "State, Flag, UpdateTime) values ('"
                        + strCompanyId + "', '"
                        + strRegisterDate + "', '"
                        + strPassengerPhone + "', '"
                        + strPassengerName + "', '"
                        + strPassengerGender + "', "
                        + nState + ", "
                        + nFlag + ", '"
                        + strUpdateTime + "')";
            }
            else if(nFlag == 2)
            {
                strSql = "update tb_passengerbaseinfo set CompanyId = '" + strCompanyId + "', RegisterDate = '" + strRegisterDate + "', PassengerPhone = '"
                        + strPassengerPhone + "', PassengerName = '" + strPassengerName + "', PassengerGender = '" + strPassengerGender + "', State = "
                        + nState + ", Flag = " + nFlag + ", UpdateTime = '" + strUpdateTime + "' where PassengerPhone = '" + strPassengerPhone + "' and CompanyId = '"+ strCompanyId + "'";
            }
            else if(nFlag == 3)
            {
                strSql = "delete from tb_passengerbaseinfo where PassengerPhone = '" + strPassengerPhone + "' and CompanyId = '"+ strCompanyId + "'";
            }
            //
            jdbcTemplate.batchUpdate(strSql);
        }
        else if(strInfoFlag.equals("orderCreate"))
        {
            String strCompanyId = obj.getString("CompanyId");
            int nAddress = obj.getIntValue("Address");
            String strOrderId = obj.getString("OrderId");
            String strDepartTime = obj.getLong("DepartTime").toString();
            String strOrderTime = obj.getLong("OrderTime").toString();
            String strPassengerNote = "";
            if(obj.containsKey("PassengerNote"))
            {
                strPassengerNote = obj.getString("PassengerNote");
            }
            String strDeparture = obj.getString("Departure");
            long dDepLongitude = obj.getLongValue("DepLongitude");
            long dDepLatitude = obj.getLongValue("DepLatitude");
            String strDestination = obj.getString("Destination");
            long dDestLongitude = obj.getLongValue("DestLongitude");
            long dDestLatitude = obj.getLongValue("DestLatitude");
            int nEncrypt = obj.getIntValue("Encrypt");
            String strFareType = obj.getString("FareType");

            strSql = "insert into tb_OrderCreate(CompanyId, Address, OrderId, DepartTime, OrderTime, PassengerNote, Departure, DepLongitude, "
                    + "DepLatitude, Destination, DestLongitude, DestLatitude, EncryptBS, FareType) values ('"
                    + strCompanyId + "', "
                    + nAddress + ", '"
                    + strOrderId + "', '"
                    + strDepartTime + "', '"
                    + strOrderTime + "', '"
                    + strPassengerNote + "', '"
                    + strDeparture + "', "
                    + dDepLongitude + ", "
                    + dDepLatitude + ", '"
                    + strDestination + "', "
                    + dDestLongitude + ", "
                    + dDestLatitude + ", "
                    + nEncrypt + ", '"
                    + strFareType + "')";

            jdbcTemplate.batchUpdate(strSql);

        }
        else if(strInfoFlag.equals("orderMatch"))
        {
            String strCompanyId = obj.getString("CompanyId");
            int nAddress = obj.getIntValue("Address");
            String strOrderId = obj.getString("OrderId");
            long dLongitude = 0;
            if(obj.containsKey("Longitude"))
            {
                dLongitude = obj.getLongValue("Longitude");
            }
            long dLatitude = 0;
            if(obj.containsKey("Latitude"))
            {
                dLatitude = obj.getLongValue("Latitude");
            }
            int nEncrypt = obj.getIntValue("Encrypt");
            String strLicenseId = obj.getString("LicenseId");
            String strDriverPhone = obj.getString("DriverPhone");
            String strVehicleNo = obj.getString("VehicleNo");
            String strDistributeTime = obj.getLong("DistributeTime").toString();

            strSql = "insert into tb_OrderMatch(CompanyId, Address, OrderId, Longitude, Latitude, EncryptBS, LicenseId, DriverPhone, VehicleNo, DistributeTime) "
                    + "values ('"
                    + strCompanyId + "', "
                    + nAddress + ", '"
                    + strOrderId + "', "
                    + dLongitude + ", "
                    + dLatitude + ", "
                    + nEncrypt + ", '"
                    + strLicenseId + "', '"
                    + strDriverPhone + "', '"
                    + strVehicleNo + "', '"
                    + strDistributeTime + "')";

            //PrintContext.print("订单成功：" + strSql);
            jdbcTemplate.batchUpdate(strSql);

        }
        else if(strInfoFlag.equals("orderCancel"))
        {
            String strCompanyId = obj.getString("CompanyId");
            int nAddress = obj.getIntValue("Address");
            String strOrderId = obj.getString("OrderId");
            String strOrderTime = "19700101000000";
            if(obj.containsKey("OrderTime"))
            {
                strOrderTime = obj.getLong("OrderTime").toString();
            }
            String strCancelTime = obj.getLong("CancelTime").toString();
            String strOperator = obj.getString("Operator");
            String strCancelTypeCode = obj.getString("CancelTypeCode");
            String strCancelReason = "";
            if(obj.containsKey("CancelReason"))
            {
                strCancelReason = obj.getString("CancelReason");
            }

            strSql = "insert into tb_OrderCancel(CompanyId, Address, OrderId, OrderTime, CancelTime, Operator, CancelTypeCode, CancelReason) values ('"
                    + strCompanyId + "', "
                    + nAddress + ", '"
                    + strOrderId + "', '"
                    + strOrderTime + "', '"
                    + strCancelTime + "', '"
                    + strOperator + "', '"
                    + strCancelTypeCode + "', '"
                    + strCancelReason + "')";
            jdbcTemplate.batchUpdate(strSql);

        }
        else if(strInfoFlag.equals("operateLogin"))
        {
            String strCompanyId = obj.getString("CompanyId");
            String strLicenseId = obj.getString("LicenseId");
            String strVehicleNo = obj.getString("VehicleNo");
            String strLoginTime = obj.getLong("LoginTime").toString();
            long dLongitude = 0;
            if(obj.containsKey("Longitude"))
            {
                dLongitude = obj.getLongValue("Longitude");
            }
            long dLatitude = 0;
            if(obj.containsKey("Latitude"))
            {
                dLatitude = obj.getLongValue("Latitude");
            }
            int nEncrypt = obj.getIntValue("Encrypt");
            strSql = "insert into tb_OperateLogin(CompanyId, LicenseId, VehicleNo, LoginTime, Longitude, Latitude, EncryptBS) values ('"
                    + strCompanyId + "', '"
                    + strLicenseId + "', '"
                    + strVehicleNo + "', '"
                    + strLoginTime + "', "
                    + dLongitude + ", "
                    + dLatitude + ", "
                    + nEncrypt + ")";
            //
            jdbcTemplate.batchUpdate(strSql);
        }
        else if(strInfoFlag.equals("operateLogout"))
        {
            String strCompanyId = obj.getString("CompanyId");
            String strLicenseId = obj.getString("LicenseId");
            String strVehicleNo = obj.getString("VehicleNo");
            String strLogoutTime = obj.getLong("LogoutTime").toString();
            long dLongitude = 0;
            if(obj.containsKey(""))
            {
                dLongitude = obj.getLongValue("Longitude");
            }
            long dLatitude = 0;
            if(obj.containsKey(""))
            {
                dLatitude = obj.getLongValue("Latitude");
            }
            int nEncrypt = obj.getIntValue("Encrypt");
            strSql = "insert into tb_OperateLogout(CompanyId, LicenseId, VehicleNo, LogoutTime, Longitude, Latitude, EncryptBS) values ('"
                    + strCompanyId + "', '"
                    + strLicenseId + "', '"
                    + strVehicleNo + "', '"
                    + strLogoutTime + "', "
                    + dLongitude + ", "
                    + dLatitude + ", "
                    + nEncrypt + ")";
            //
            jdbcTemplate.batchUpdate(strSql);
        }
        else if(strInfoFlag.equals("operateDepart"))
        {
            String strCompanyId = obj.getString("CompanyId");
            String strOrderId = obj.getString("OrderId");
            String strLicenseId = obj.getString("LicenseId");
            String strFareType = obj.getString("FareType");
            String strVehicleNo = obj.getString("VehicleNo");
            long dDepLongitude = obj.getLongValue("DepLongitude");
            long dDepLatitude = obj.getLongValue("DepLatitude");
            int nEncrypt = obj.getIntValue("Encrypt");
            String strDepTime = obj.getLong("DepTime").toString();
            double dWaitMile = 0;
            if(obj.containsKey("WaitMile"))
            {
                dWaitMile = obj.getDoubleValue("WaitMile");
            }
            int nWaitTime = 0;
            if(obj.containsKey("WaitTime"))
            {
                nWaitTime = obj.getIntValue("WaitTime");
            }
            strSql = "insert into tb_OperateDepart(CompanyId, OrderId, LicenseId, FareType, VehicleNo, DepLongitude, DepLatitude, EncryptBS, DepTime, "
                    + "WaitMile, WaitTime) values ('"
                    + strCompanyId + "', '"
                    + strOrderId + "', '"
                    + strLicenseId + "', '"
                    + strFareType + "', '"
                    + strVehicleNo + "', "
                    + dDepLongitude + ", "
                    + dDepLatitude+ ", "
                    + nEncrypt + ", '"
                    + strDepTime + "', "
                    + dWaitMile + ", "
                    + nWaitTime + ")";
            //
            jdbcTemplate.batchUpdate(strSql);
        }
        else if(strInfoFlag.equals("operateArrive"))
        {
            String strCompanyId = obj.getString("CompanyId");
            String strOrderId = obj.getString("OrderId");
            long dDestLongitude = obj.getLongValue("DestLongitude");
            long dDestLatitude = obj.getLongValue("DestLatitude");
            int nEncrypt = obj.getIntValue("Encrypt");
            String strDestTime = obj.getLong("DestTime").toString();
            double dDriveMile = obj.getDoubleValue("DriveMile");
            int nDriveTime = obj.getIntValue("DriveTime");
            strSql = "insert into tb_OperateArrive(CompanyId, OrderId, DestLongitude, DestLatitude, EncryptBS, DestTime, DriveMile, DriveTime) values ('"
                    + strCompanyId + "', '"
                    + strOrderId + "', "
                    + dDestLongitude + ", "
                    + dDestLatitude + ", "
                    + nEncrypt + ", '"
                    + strDestTime + "', "
                    + dDriveMile + ", "
                    + nDriveTime + ")";
            //
            jdbcTemplate.batchUpdate(strSql);
        }
        else if(strInfoFlag.equals("operatePay"))
        {
            String strCompanyId = obj.getString("CompanyId");
            String strOrderId = obj.getString("OrderId");
            int nOnArea = obj.getIntValue("OnArea");
            String strDriverName = "";
            if(obj.containsKey("DriverName"))
            {
                strDriverName = obj.getString("DriverName");
            }
            String strLicenseId = obj.getString("LicenseId");
            String strFareType = obj.getString("FareType");
            String strVehicleNo = obj.getString("VehicleNo");
            String strBookDepTime = obj.getLong("BookDepTime").toString();
            int nWaitTime = 0;
            if(obj.containsKey("WaitTime"))
            {
                nWaitTime = obj.getIntValue("WaitTime");
            }
            long dDepLongitude = obj.getLongValue("DepLongitude");
            long dDepLatitude = obj.getLongValue("DepLatitude");
            String strDepArea = "";
            if(obj.containsKey("DepArea"))
            {
                strDepArea = obj.getString("DepArea");
            }

            String strDepTime = obj.getLong("DepTime").toString();
            long dDestLongitude = obj.getLongValue("DestLongitude");
            long dDestLatitude = obj.getLongValue("DestLatitude");
            String strDestArea = "";
            if(obj.containsKey("DestArea"))
            {
                strDestArea = obj.getString("DestArea");
            }
            String strDestTime = obj.getLong("DestTime").toString();

            String strBookModel = "";
            if(obj.containsKey("BookModel"))
            {
                strBookModel = obj.getString("BookModel");
            }

            String strModel = "";
            if(obj.containsKey("Model"))
            {
                strModel = obj.getString("Model");
            }

            double dDriveMile = obj.getDoubleValue("DriveMile");
            int nDriveTime = obj.getIntValue("DriveTime");
            double dWaitMile = 0;
            if(obj.containsKey("WaitMile"))
            {
                dWaitMile = obj.getDoubleValue("WaitMile");
            }
            double dFactPrice = obj.getDoubleValue("FactPrice");
            double dPrice = 0;
            if(obj.containsKey("Price"))
            {
                dPrice = obj.getDoubleValue("Price");
            }
            double dCashPrice = 0;
            if(obj.containsKey("CashPrice"))
            {
                dCashPrice = obj.getDoubleValue("CashPrice");
            }
            String strLineName = "";
            if(obj.containsKey("LineName"))
            {
                strLineName = obj.getString("LineName");
            }
            double dLinePrice = 0;
            if(obj.containsKey("LinePrice"))
            {
                dLinePrice= obj.getDoubleValue("LinePrice");
            }
            String strPosName = "";
            if(obj.containsKey("PosName"))
            {
                strPosName = obj.getString("PosName");
            }
            double dPosPrice = 0;
            if(obj.containsKey("PosPrice"))
            {
                dPosPrice= obj.getDoubleValue("PosPrice");
            }
            double dBenfitPrice = 0;
            if(obj.containsKey("BenfitPrice"))
            {
                dBenfitPrice = obj.getDoubleValue("BenfitPrice");
            }
            double dBookTip = 0;
            if(obj.containsKey("BookTip"))
            {
                dBookTip= obj.getDoubleValue("BookTip");
            }
            double dPassengerTip = 0;
            if(obj.containsKey("PassengerTip"))
            {
                dPassengerTip = obj.getDoubleValue("PassengerTip");
            }
            double dPeakUpPrice = 0;
            if(obj.containsKey("PeakUpPrice"))
            {
                dPeakUpPrice= obj.getDoubleValue("PeakUpPrice");
            }
            double dNightUpPrice = 0;
            if(obj.containsKey("NightUpPrice"))
            {
                dNightUpPrice = obj.getDoubleValue("NightUpPrice");
            }
            double dFarUpPrice = 0;
            if(obj.containsKey("FarUpPrice"))
            {
                dFarUpPrice= obj.getDoubleValue("FarUpPrice");
            }
            double dOtherUpPrice = 0;
            if(obj.containsKey("OtherUpPrice"))
            {
                dOtherUpPrice = obj.getDoubleValue("OtherUpPrice");
            }
            String strPayState = obj.getString("PayState");

            String strPayTime = "19700101000000";
            if(obj.containsKey("PayTime"))
            {

                if(obj.getLong("PayTime") == 0)
                {
                    strPayTime = strDestTime;
                }
                else
                {
                    strPayTime = obj.getLong("PayTime").toString();
                }
            }
            String strOrderMatchTime = "19700101000000";
            if(obj.containsKey("OrderMatchTime"))
            {
                if(obj.getLong("OrderMatchTime") == 0)
                {
                    strOrderMatchTime = strDestTime;
                }
                else
                {
                    strOrderMatchTime = obj.getLong("OrderMatchTime").toString();
                }

            }
            String strInvoiceStatus = obj.getString("InvoiceStatus");

            strSql = "insert into tb_OperatePay(CompanyId, OrderId, OnArea, DriverName, LicenseId, FareType, VehicleNo, BookDepTime, WaitTime, DepLongitude,"
                    + "DepLatitude, DepArea, DepTime, DestLongitude, DestLatitude, DestArea, DestTime, BookModel, Model, DriveMile, DriveTime, WaitMile, FactPrice,"
                    + "Price, CashPrice, LineName, LinePrice, PosName, PosPrice, BenfitPrice, BookTip, PassengerTip, PeakUpPrice, NightUpPrice, FarUpPrice, OtherUpPrice,"
                    + "PayState, PayTime, OrderMatchTime, InvoiceStatus) values ('"
                    + strCompanyId + "', '"
                    + strOrderId + "', "
                    + nOnArea + ", '"
                    + strDriverName + "', '"
                    + strLicenseId + "', '"
                    + strFareType + "', '"
                    + strVehicleNo +"', '"
                    + strBookDepTime + "', "
                    + nWaitTime + ", "
                    + dDepLongitude + ", "
                    + dDepLatitude + ", '"
                    + strDepArea + "', '"
                    + strDepTime + "', "
                    + dDestLongitude + ", "
                    + dDestLatitude + ", '"
                    + strDestArea + "', '"
                    + strDestTime + "', '"
                    + strBookModel + "', '"
                    + strModel + "', "
                    + dDriveMile + ", "
                    + nDriveTime + ", "
                    + dWaitMile + ", "
                    + dFactPrice + ", "
                    + dPrice + ", "
                    + dCashPrice + ", '"
                    + strLineName + "', "
                    + dLinePrice + ", '"
                    + strPosName + "', "
                    + dPosPrice + ", "
                    + dBenfitPrice + ", "
                    + dBookTip + ", "
                    + dPassengerTip + ", "
                    + dPeakUpPrice + ", "
                    + dNightUpPrice + ", "
                    + dFarUpPrice + ", "
                    + dOtherUpPrice + ", '"
                    + strPayState + "', '"
                    + strPayTime + "', '"
                    + strOrderMatchTime + "', '"
                    + strInvoiceStatus + "')";

            jdbcTemplate.batchUpdate(strSql);

        }
        else if(strInfoFlag.equals("ratedPassenger"))
        {
            String strCompanyId = obj.getString("CompanyId");
            String strOrderId = obj.getString("OrderId");
            String strEvaluateTime = obj.getLong("EvaluateTime").toString();
            int nServiceScore= obj.getIntValue("ServiceScore");
            int nDriverScore = 0;
            if(obj.containsKey("DriverScore"))
            {
                nDriverScore = obj.getIntValue("DriverScore");
            }
            int nVehicleScore = 0;
            if(obj.containsKey("VehicleScore"))
            {
                nVehicleScore= obj.getIntValue("VehicleScore");
            }
            String strDetail = "";
            if(obj.containsKey("Detail"))
            {
                strDetail = obj.getString("Detail");
            }
            strSql = "insert into tb_RatedPassenger(CompanyId, OrderId, EvaluateTime, ServiceScore, DriverScore, VehicleScore, Detail) values ('"
                    + strCompanyId + "', '"
                    + strOrderId + "', '"
                    + strEvaluateTime + "', "
                    + nServiceScore + ", "
                    + nDriverScore + ", "
                    + nVehicleScore + ", '"
                    + strDetail + "')";
            //
            jdbcTemplate.batchUpdate(strSql);
        }
        else if(strInfoFlag.equals("ratedPassengerComplaint"))
        {
            String strCompanyId = obj.getString("CompanyId");
            String strOrderId = obj.getString("OrderId");
            String strComplaintTime = obj.getLong("ComplaintTime").toString();
            String strDetail = obj.getString("Detail");
            String strResult = "";
            if(obj.containsKey("Result"))
            {
                strResult = obj.getString("Result");
            }
            strSql = "insert into tb_RatedPassengerComplaint(CompanyId, OrderId, ComplaintTime, Detail, Result) values ('"
                    + strCompanyId + "', '"
                    + strOrderId + "', '"
                    + strComplaintTime + "', '"
                    + strDetail + "', '"
                    + strResult + "')";
            //
            jdbcTemplate.batchUpdate(strSql);
        }
        else if(strInfoFlag.equals("ratedDriverPunish"))
        {
            String strCompanyId = obj.getString("CompanyId");
            String strLicenseId = obj.getString("LicenseId");
            String strPunishTime= obj.getString("PunishTime");
            String strPunishReason= "";
            if(obj.containsKey("PunishReason"))
            {
                strPunishReason = obj.getString("PunishReason");
            }
            String strPunishResult = obj.getString("PunishResult");
            strSql = "insert into tb_ratedDriverPunish(CompanyId, LicenseId, PunishTime, PunishReason, PunishResult) values ('"
                    + strCompanyId + "', '"
                    + strLicenseId + "', '"
                    + strPunishTime + "', '"
                    + strPunishReason + "', '"
                    + strPunishResult + "')";
            //
            jdbcTemplate.batchUpdate(strSql);
        }
        else if(strInfoFlag.equals("ratedDriver"))
        {
            String strCompanyId = obj.getString("CompanyId");
            String strLicenseId = obj.getString("LicenseId");
            int nLevel = obj.getIntValue("Level");
            String strTestDate = obj.getLong("TestDate").toString();
            String strTestDepartment = obj.getString("TestDepartment");
            strSql = "insert into tb_ratedDriver(CompanyId, LicenseId, Level, TestDate, TestDepartment) values ('"
                    + strCompanyId + "', '"
                    + strLicenseId + "', "
                    + nLevel+ ", '"
                    + strTestDate + "', '"
                    + strTestDepartment + "')";
            //
            jdbcTemplate.batchUpdate(strSql);
        }
        else if(strInfoFlag.equals("shareCompany"))
        {
            String strCompanyId = obj.getString("CompanyId");
            String strCompanyName = obj.getString("CompanyName");
            String strIdentifier = obj.getString("Identifier");
            int nAddress = obj.getIntValue("Address");
            String strContactAddress = obj.getString("ContactAddress");
            String strEconomicType = obj.getString("EconomicType");
            String strLegalName = obj.getString("LegalName");
            String strLegalPhone = obj.getString("LegalPhone");
            int nState = obj.getIntValue("State");
            int nFlag = obj.getIntValue("Flag");
            String strUpdateTime = obj.getLong("UpdateTime").toString();
            if(nFlag == 1)
            {
                strSql = "insert into tb_shareCompany(CompanyId, CompanyName, Identifier, Address, ContactAddress, EconomicType, LegalName, LegalPhone, State, "
                        + "Flag, UpdateTime) values ('"
                        + strCompanyId + "', '"
                        + strCompanyName + "', '"
                        + strIdentifier + "', "
                        + nAddress + ", '"
                        + strContactAddress + "', '"
                        + strEconomicType + "', '"
                        + strLegalName + "', '"
                        + strLegalPhone + "', "
                        + nState + ", "
                        + nFlag + ", '"
                        + strUpdateTime + "')";
            }
            else if(nFlag == 2)
            {
                strSql = "update tb_shareCompany set CompanyId = '" + strCompanyId + "', CompanyName = '" + strCompanyName + "', Identifier = '"
                        + strIdentifier + "', Address = " + nAddress + ", ContactAddress = '" + strContactAddress + "', EconomicType = '"
                        + strEconomicType + "', LegalName = '" + strLegalName + "', LegalPhone = '" + strLegalPhone + "', State = "
                        + nState + ", Flag = " + nFlag + ", UpdateTime = '" + strUpdateTime + "' where Identifier = '" + strIdentifier + "' and CompanyId = '"+ strCompanyId + "'";
            }
            else if(nFlag == 3)
            {
                strSql = "delete from tb_shareCompany where Identifier = '" + strIdentifier + "' and CompanyId = '"+ strCompanyId + "'";
            }
            //
            jdbcTemplate.batchUpdate(strSql);
        }
        else if(strInfoFlag.equals("shareRoute"))
        {
            String strCompanyId = obj.getString("CompanyId");
            int nAddress = 0;
            if(obj.containsKey("Address"))
            {
                nAddress = obj.getIntValue("Address");
            }
            String strRouteId = obj.getString("RouteId");
            String strDriverName = obj.getString("DriverName");
            String strDriverPhone = obj.getString("DriverPhone");
            String strLicenseId = obj.getString("LicenseId");
            String strVehicleNo = obj.getString("VehicleNo");
            String strDeparture = obj.getString("Departure");
            long dDepLongitude = obj.getLongValue("DepLongitude");
            long dDepLatitude = obj.getLongValue("DepLatitude");
            String strDestination = obj.getString("Destination");
            long dDestLongitude = obj.getLongValue("DestLongitude");
            long dDestLatitude = obj.getLongValue("DestLatitude");
            int nEncrypt = obj.getIntValue("Encrypt");
            String strRouteCreateTime = obj.getLong("RouteCreateTime").toString();
            double dRouteMile = 0;
            if(obj.containsKey("RouteMile"))
            {
                dRouteMile = obj.getDoubleValue("RouteMile");
            }
            String strRouteNote= "";
            if(obj.containsKey("RouteNote"))
            {
                strRouteNote = obj.getString("RouteNote");
            }
            strSql = "insert into tb_shareRoute(CompanyId, Address, RouteId, DriverName, DriverPhone, LicenseId, VehicleNo, Departure, DepLongitude, DepLatitude,"
                    + "Destination, DestLongitude, DestLatitude, EncryptBS, RouteCreateTime, RouteMile, RouteNote) values ('"
                    + strCompanyId + "', "
                    + nAddress + ", '"
                    + strRouteId + "', '"
                    + strDriverName + "', '"
                    + strDriverPhone + "', '"
                    + strLicenseId + "', '"
                    + strVehicleNo + "', '"
                    + strDeparture + "', "
                    + dDepLongitude + ", "
                    + dDepLatitude + ", '"
                    + strDestination + "', "
                    + dDestLongitude + ", "
                    + dDestLatitude + ", "
                    + nEncrypt + ", '"
                    + strRouteCreateTime + "', "
                    + dRouteMile + ", '"
                    + strRouteNote + "')";
            //
            jdbcTemplate.batchUpdate(strSql);
        }
        else if(strInfoFlag.equals("shareOrder"))
        {
            String strCompanyId = obj.getString("CompanyId");
            int nAddress = obj.getIntValue("Address");
            String strRouteId = obj.getString("RouteId");
            String strOrderId = obj.getString("OrderId");
            String strBookDepartTime = obj.getLong("BookDepartTime").toString();
            String strDeparture = obj.getString("Departure");
            long dDepLongitude = obj.getLongValue("DepLongitude");
            long dDepLatitude = obj.getLongValue("DepLatitude");
            String strDestination = obj.getString("Destination");
            long dDestLongitude = obj.getLongValue("DestLongitude");
            long dDestLatitude = obj.getLongValue("DestLatitude");
            int nEncrypt = obj.getIntValue("Encrypt");
            String strOrderEnsureTime = obj.getLong("OrderEnsureTime").toString();
            int nPassengerNum = obj.getIntValue("PassengerNum");
            String strPassengerNote = "";
            if(obj.containsKey("PassengerNote"))
            {
                strPassengerNote = obj.getString("PassengerNote");
            }
            strSql = "insert into tb_ShareOrder(CompanyId, Address, RouteId, OrderId, BookDepartTime, Departure, DepLongitude, DepLatitude, Destination,"
                    + "DestLongitude, DestLatitude, EncryptBS, OrderEnsureTime, PassengerNum, PassengerNote) values ('"
                    + strCompanyId + "', "
                    + nAddress + ", '"
                    + strRouteId + "', '"
                    + strOrderId + "', '"
                    + strBookDepartTime + "', '"
                    + strDeparture + "', "
                    + dDepLongitude + ", "
                    + dDepLatitude + ", '"
                    + strDestination +"', "
                    + dDestLongitude + ", "
                    + dDestLatitude + ", "
                    + nEncrypt + ", '"
                    + strOrderEnsureTime + "', "
                    + nPassengerNum + ", '"
                    + strPassengerNote + "')";
            //
            jdbcTemplate.batchUpdate(strSql);
        }
        else if(strInfoFlag.equals("sharePay"))
        {
            String strCompanyId = obj.getString("CompanyId");
            int nAddress = obj.getIntValue("Address");
            String strRouteId = obj.getString("RouteId");
            String strOrderId = obj.getString("OrderId");
            String strDriverPhone = obj.getString("DriverPhone");
            String strLicenseId = obj.getString("LicenseId");
            String strVehicleNo = obj.getString("VehicleNo");
            String strFareType = obj.getString("FareType");
            String strBookDepartTime = obj.getLong("BookDepartTime").toString();
            String strDepartTime = obj.getLong("DepartTime").toString();
            String strDeparture = obj.getString("Departure");
            long dDepLongitude = obj.getLongValue("DepLongitude");
            long dDepLatitude = obj.getLongValue("DepLatitude");
            String strDestTime = obj.getLong("DestTime").toString();
            String strDestination = obj.getString("Destination");
            long dDestLongitude = obj.getLongValue("DestLongitude");
            long dDestLatitude = obj.getLongValue("DestLatitude");
            int nEncrypt = obj.getIntValue("Encrypt");
            double dDriveMile = obj.getDoubleValue("DriveMile");
            int nDriveTime = obj.getIntValue("DriveTime");
            double dFactPrice = obj.getDoubleValue("FactPrice");
            double dPrice = obj.getDoubleValue("Price");
            double dCashPrice = obj.getDoubleValue("CashPrice");
            String strLineName = "";
            if(obj.containsKey("LineName"))
            {
                strLineName = obj.getString("LineName");
            }
            double dLinePrice= obj.getDoubleValue("LinePrice");
            double dBenfitPrice = 0;
            if(obj.containsKey("BenfitPrice"))
            {
                dBenfitPrice = obj.getDoubleValue("BenfitPrice");
            }
            double dShareFuelFee = 0;
            if(obj.containsKey("ShareFuelFee"))
            {
                dShareFuelFee = obj.getDoubleValue("ShareFuelFee");
            }
            double dShareHighwayToll = 0;
            if(obj.containsKey("ShareHighwayToll"))
            {
                dShareHighwayToll = obj.getDoubleValue("ShareHighwayToll");
            }
            double dPassengerTip = 0;
            if(obj.containsKey("PassengerTip"))
            {
                dPassengerTip = obj.getDoubleValue("PassengerTip");
            }
            double dShareOther = 0;
            if(obj.containsKey("ShareOther"))
            {
                dShareOther = obj.getDoubleValue("ShareOther");
            }
            String strPayState = obj.getString("PayState");
            int nPassengerNum = obj.getIntValue("PassengerNum");
            String strPayTime = "19700101000000";
            if(obj.containsKey("PayTime"))
            {
                strPayTime = obj.getLong("PayTime").toString();
            }
            String strOrderMatchTime = "19700101000000";
            if(obj.containsKey("OrderMatchTime"))
            {
                strOrderMatchTime = obj.getLong("OrderMatchTime").toString();
            }

            strSql = "insert into tb_sharePay(CompanyId, Address, RouteId, OrderId, DriverPhone, LicenseId, VehicleNo, FareType, BookDepartTime, DepartTime,"
                    + " Departure, DepLongitude, DepLatitude, DestTime, Destination, DestLongitude, DestLatitude, EncryptBS, DriveMile, DriveTime, FactPrice,"
                    + " Price, CashPrice, LineName, LinePrice, BenfitPrice, ShareFuelFee, ShareHighwayToll, PassengerTip, ShareOther, PayState, PassengerNum, "
                    + " PayTime, OrderMatchTime) values ('"
                    + strCompanyId + "', "
                    + nAddress + ", '"
                    + strRouteId + "', '"
                    + strOrderId + "', '"
                    + strDriverPhone + "', '"
                    + strLicenseId + "', '"
                    + strVehicleNo + "', '"
                    + strFareType + "', '"
                    + strBookDepartTime + "', '"
                    + strDepartTime + "', '"
                    + strDeparture + "', "
                    + dDepLongitude + ", "
                    + dDepLatitude + ", '"
                    + strDestTime + "', '"
                    + strDestination + "', "
                    + dDestLongitude + ", "
                    + dDestLatitude + ", "
                    + nEncrypt + ", "
                    + dDriveMile + ", "
                    + nDriveTime + ", "
                    + dFactPrice + ", "
                    + dPrice + ", "
                    + dCashPrice + ", '"
                    + strLineName + "', "
                    + dLinePrice + ", "
                    + dBenfitPrice + ", "
                    + dShareFuelFee + ", "
                    + dShareHighwayToll + ", "
                    + dPassengerTip + ", "
                    + dShareOther + ", '"
                    + strPayState + "', "
                    + nPassengerNum + ", '"
                    + strPayTime + "', '"
                    + strOrderMatchTime + "')";
            //
            jdbcTemplate.batchUpdate(strSql);
        }
    }

    public static JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public static void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        ProcessStaticRunning.jdbcTemplate = jdbcTemplate;
    }

    public ConcurrentQueue getQueue() {
        return queue;
    }

    public void setQueue(ConcurrentQueue queue) {
        this.queue = queue;
    }
}
