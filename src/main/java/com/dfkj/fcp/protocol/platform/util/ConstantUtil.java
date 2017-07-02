package com.dfkj.fcp.protocol.platform.util;

import com.dfkj.fcp.core.constant.EAction;
import com.dfkj.fcp.core.constant.EDeviceStatus;
import com.dfkj.fcp.core.constant.EDeviceType;
import com.dfkj.fcp.core.constant.EMessageType;

/**
 * 常量帮助类
 *
 * Created by JiangWenGuang on 2017/4/23.
 */
public class ConstantUtil {

    /**
     * 根据值转换为动作枚举
     *
     * @param action
     * @return
     */
    public static EAction convertEActionByValue(int action) {
        switch (action) {
            case 0: return EAction.CLOSE; 	//	关闭/停止
            case 1: return EAction.OPEN;
            default:
                return EAction.NO_ACTION;
        }
    }

    /**
     * 将设备状态转换为值
     *
     * @param status
     * @return 1:故障,未接等情况 0:正常
     */
    public static int convertValueByEDeviceStatus(EDeviceStatus status) {
        if (status == EDeviceStatus.NO_ACCESS || status == EDeviceStatus.TROUBLE) {
            return 1;
        }
        return 0;
    }

    /**
     * 将控制器设备运行状态转换成值
     *
     * @param status
     * @return
     */
    public static double convertRunStatusByEDeviceStatus(EDeviceStatus status) {
        switch (status) {
            case OPEN: return 1.0;
            case OPENING: return 3.0;
            case STOP: return 7.0;
            case CLOSING: return 2.0;
            case CLOSE: return 0.0;
            case RUNNING: return 4.0;
            default:
                break;
        }
        return 0.0;
    }

    /**
     * 将设备类型转换为值 (参考T_B_DEVICETYPE表的值）
     *
     * @param devType
     * @return
     */
    public static int convertValueByEDeviceType(EDeviceType devType) {
        switch (devType) {
		/* 传感器 */
            case AIR_TEMPERATURE: return 1;
            case AIR_HUMIDITY: return 2;
            case CO2: return 6;		//Co2
            //case ELECTRICITY:		//	电量
            //case VOLTAGE(203),			//	电压
            case O2: return 12;				//	氧含量
            case WIND_SPEED: return 10;		//	风速
            case RAINFALL: return 11;		//	雨量
            case SOIL_TEMPERATURE: return 4;	//	土壤温度
            case SOIL_HUMIDITY: return 5;	//	土壤水分
            case PH: return 17;			//	PH值
            case ILLUMINATION: return 9;	//	照度
            case WIND_DIRECTION: return 7;		//	风向
            //case WATER_TEMPERATURE: return 	//	水温
            //case SALINITY(213),		//	盐分
            case LIQUID_LVL: return 19;		//	液位
            case WATER_GAGE:return 18;//液压
            case WATER_YIELD:return 20;//液量
            case LIQUID_LEVEL_STATE: return 21; // 液位状态
            case CONDUCTIVITY: return 22; // 电导率

		    /* 控制器 */
            case GENERAL_SWITCH:
            case TRIGGER_SWITCH: return 30;
            case STATUS_SWITCH: return 28;
            case CTL_SWITCH: return 29;
            case MECHANICAL_ROTATION_SWITCH: return 205;

            default:
                return 0;
        }
    }

    /**
     * 将值转换为设备类型
     *
     * @param devt
     * @return
     */
    public static EDeviceType convertEDeiceTypeByValue(int devt) {
        switch (devt) {
		   /* 传感器 */
            case 1:
                return EDeviceType.AIR_TEMPERATURE;
            case 2:
                return EDeviceType.AIR_HUMIDITY;
            //case ELECTRICITY:		//	电量
            //case VOLTAGE(203),			//	电压
            case 12:
                return EDeviceType.O2;                //	氧含量
            case 6:
                return EDeviceType.CO2;
            case 10:
                return EDeviceType.WIND_SPEED;        //	风速
            case 11:
                return EDeviceType.RAINFALL;        //	雨量
            case 4:
                return EDeviceType.SOIL_TEMPERATURE;    //	土壤温度
            case 5:
                return EDeviceType.SOIL_HUMIDITY;    //	土壤水分
            case 17:
                return EDeviceType.PH;            //	PH值
            case 9:
                return EDeviceType.ILLUMINATION;    //	照度
            case 7:
                return EDeviceType.WIND_DIRECTION;        //	风向
            //case WATER_TEMPERATURE: return 	//	水温
            //case SALINITY(213),		//	盐分
            case 19:
                return EDeviceType.LIQUID_LVL;        //	液位
            case 21:
                return EDeviceType.LIQUID_LEVEL_STATE; // 液位状态
            case 22:
                return EDeviceType.CONDUCTIVITY; // 电导率

		     /* 控制器 */
            case 30:
                return EDeviceType.TRIGGER_SWITCH;
            case 28:
                return EDeviceType.STATUS_SWITCH;
            case 29:
                return EDeviceType.CTL_SWITCH;
            case 205:
                return EDeviceType.MECHANICAL_ROTATION_SWITCH;

            default:
                return EDeviceType.UNKNOWN;
        }
    }

    /**
     * 将消息类型转换为值
     *
     * @param msgType
     * @return
     */
    public static int convertValueByEMessageType(EMessageType msgType) {
        if (msgType == EMessageType.CTRL_ACTION) {
            return 6;
        }
        else if (msgType == EMessageType.SENSOR_DATA) {
            return 3;
        }
        else if (msgType == EMessageType.CTRL_REPORT_STATUS) {
            return 5;
        }
        else if (msgType == EMessageType.GATEWAY_HEARTBEAT) {
            return 0;
        }

        return 0;
    }

    public static EMessageType convertEMessageTypeByValue(int mstType){
        switch(mstType) {
            case 0: return EMessageType.GATEWAY_HEARTBEAT;
            case 3: return EMessageType.SENSOR_DATA;
            case 5: return EMessageType.CTRL_REPORT_STATUS;
            case 6:
                return EMessageType.CTRL_ACTION_RESP;
            default:
                return EMessageType.UNKNOWN;
        }
    }



}
