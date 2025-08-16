package com.blockchain.common.values;

public class Permissions {
	public static final String SP_BASIC_G = "100004";// 全国商城基本权限
	public static final String SP_BASIC_R = "200004";// 地方商城基本权限
  public static final String SP_ROOT_G = "100008";// 全国商城超级权限
  public static final String SP_ROOT_R = "200008";// 地方商城超级权限
	public static final String SP_COMMODITY_B2B_G1 = "113011";// B2B全国商城发布商品权限
	public static final String SP_COMMODITY_B2B_R1 = "213031";// B2B地方商城发布商品权限
	public static final String SP_COMMODITY_B2C_G1 = "113051";// B2C全国商城发布商品权限
	public static final String SP_COMMODITY_B2C_R1 = "213071";// B2C地方商城发布商品权限
	public static final String SP_FINANCE_WITHDRAW_G1 = "121111";// 全国商城提现权限
	public static final String SP_FINANCE_WITHDRAW_R1 = "221111";// 地方商城提现权限
	public static final String SP_FINANCE_REPORT_G4 = "121814";// 全国商城查看财务报表权限
	public static final String SP_FINANCE_REPORT_R4 = "221814";// 地方商城查看财务报表权限
	public static final String SP_ORDER_REPORT_G4 = "119814";// 全国商城查看销售报表权限
	public static final String SP_ORDER_REPORT_R4 = "219814";// 地方商城查看销售报表权限

	public static final String HQ_BASIC = "300004";// 基本权限
  public static final String HQ_ROOT = "300008";// 超级权限
	public static final String HQ_COMMODITY_APPROVE = "313217";// 审批商品发布
	public static final String HQ_SUPPLIER_APPROVE = "325217";// 审批供应商加盟权限
	public static final String HQ_OPERATOR_APPROVE = "329217";// 审批运营中心加盟权限
	public static final String HQ_SERVICE_STATION_APPROVE = "331217";// 审批社区店加盟权限
	public static final String HQ_FINANCE_WITHDRAW_APPROVE = "321217";// 审批提现
	public static final String HQ_FINANCE_REPORT = "321814";// 查看财务报表权限
	public static final String HQ_ORDER_REPORT = "319814";// 查看销售报表权限
  public static final String HQ_PHONE_NO_SEARCH = "357007";// 电话查看权限
	public static final String HQ_IDEA = "379814";// 操作创意权限

	public static final String OP_BASIC = "500004";// 基本权限
  public static final String OP_ROOT = "500008";// 超级权限
  public static final String OP_COMMODITY_APPROVE = "513217";// 审批商品发布
	public static final String OP_COMMODITY_B2B_G4 = "513014";// B2B全国商城查看价格权限
	public static final String OP_ORDER_B2B_G1 = "519011";// B2B全国商城下单权限
  public static final String OP_ORDER_REPORT = "519814";// 查看销售报表权限
	public static final String OP_FINANCE = "521814";// 查看财务报表权限
  public static final String OP_SALEMAN = "531001";// 业务员权限

	public static final String SS_BASIC = "700004";//基本权限 
  public static final String SS_ROOT = "700008";// 超级权限
	public static final String SS_COMMODITY_B2B_R4 = "713034";// B2B地方商城查看价格权限
	public static final String SS_ORDER_B2B_R1 = "719031";// B2B地方商城下单权限
  public static final String SS_ORDER_REPORT = "719814";// 查看销售报表权限
	public static final String SS_FINANCE = "721814";// 查看财务报表权限
  public static final String SS_DELIVERMAN = "731001";// 送货员权限

	public static final String MB_BASIC = "900004";// 基本权限
  public static final String MB_WITHDRAW = "900104";// 提现权限
  public static final String MB_DRAW_UPLOAD = "903003";// 上传权限
  public static final String MB_DRAW_DETECT = "903007";// 检测权限
  public static final String MB_PINAO_UPLOAD = "905003";// 上传权限
  public static final String MB_PINAO_DETECT = "905007";// 检测权限
  public static final String MB_IDEA_SEARCH = "907007";// 创意查看权限

}
