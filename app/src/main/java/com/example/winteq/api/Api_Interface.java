package com.example.winteq.api;

import com.example.winteq.model.asset.AssetData;
import com.example.winteq.model.asset.AssetResponseData;
import com.example.winteq.model.help.elektrik.HelpDataElc;
import com.example.winteq.model.help.elektrik.HelpResponseDataElc;
import com.example.winteq.model.help.mekanik.HelpDataMec;
import com.example.winteq.model.help.mekanik.HelpResponseDataMec;
import com.example.winteq.model.history.HistoryData;
import com.example.winteq.model.history.HistoryResponseData;
import com.example.winteq.model.monitoring.MonData;
import com.example.winteq.model.monitoring.MonResponseData;
import com.example.winteq.model.user.UserData;
import com.example.winteq.model.wms.WmsData;
import com.example.winteq.model.wms.WmsResponseData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

//agar bisa menghit api atau mengirim data seperti di postman
public interface Api_Interface {

    @FormUrlEncoded
    @POST("login.php")
        //pilih login yang di model <login>
    Call<UserData> loginResponse(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("register.php")
    Call<UserData> registerResponse(
            @Field("npk") String npk,
            @Field("username") String username,
            @Field("password") String password,
            @Field("fullname") String fullname,
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("forgot.php")
    Call<UserData> forgotResponse(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("profile.php")
    Call<UserData> profileResponse(
            @Field("fullname") String fullname,
            @Field("email") String email,
            @Field("npk") String npk,
            @Field("username") String username,
            @Field("no_telp") String no_telp,
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("image.php")
    Call<UserData> imageResponse(
            @Field("id") String id,
            @Field("image") String image
    );

    @FormUrlEncoded
    @POST("wmseditimage.php")
    Call<WmsData> wmseditimageResponse(
            @Field("wms_id") String wms_id,
            @Field("image") String image
    );

    @FormUrlEncoded
    @POST("userstat.php")
    Call<UserData> userstatResponse(
            @Field("id") String id,
            @Field("stat") String stat
    );

    @FormUrlEncoded
    @POST("wmsdelete.php")
    Call<WmsData> wmsdeleteData(
            @Field("wms_id") String wms_id
    );

    @FormUrlEncoded
    @POST("helpelcdelete.php")
    Call<HelpDataElc> helpelcdeleteData(
            @Field("help_elc_id") String help_elc_id
    );

    @FormUrlEncoded
    @POST("helpmecdelete.php")
    Call<HelpDataMec> helpmecdeleteData(
            @Field("help_mec_id") String help_mec_id
    );

    @FormUrlEncoded
    @POST("wmsadd.php")
    Call<WmsData> wmsaddResponse(
            @Field("qty") String qty,
            @Field("item_name") String item_name,
            @Field("type") String type,
            @Field("lifetime_wms") String lifetime_wms,
            @Field("category") String category,
            @Field("unit") String unit,
            @Field("area") String area,
            @Field("cabinet") String cabinet,
            @Field("shelf") String shelf,
            @Field("description") String description,
            @Field("image") String image
    );

    @FormUrlEncoded
    @POST("wmsupdate.php")
    Call<WmsData> wmsupdateData(
            @Field("wms_id") String wms_id,
            @Field("qty") String qty,
            @Field("item_name") String item_name,
            @Field("type") String type,
            @Field("lifetime_wms") String lifetime_wms,
            @Field("category") String category,
            @Field("unit") String unit,
            @Field("area") String area,
            @Field("cabinet") String cabinet,
            @Field("shelf") String shelf,
            @Field("description") String description,
            @Field("image") String image
    );

    @FormUrlEncoded
    @POST("helpelcupdate.php")
    Call<HelpDataElc> helpelcupdateData(
            @Field("help_elc_id") String help_elc_id,
            @Field("item_elc") String item_elc,
            @Field("desc_elc") String desc_elc,
            @Field("pdf_elc_image") String pdf_elc_image,
            @Field("pdf_elc") String pdf_elc
    );

    @FormUrlEncoded
    @POST("helpmecupdate.php")
    Call<HelpDataMec> helpmecupdateData(
            @Field("help_mec_id") String help_mec_id,
            @Field("item_mec") String item_mec,
            @Field("desc_mec") String desc_mec,
            @Field("pdf_mec_image") String pdf_mec_image,
            @Field("pdf_mec") String pdf_mec
    );

    @FormUrlEncoded
    @POST("helpelcadd.php")
    Call<HelpDataElc> helpelcaddResponse(
            @Field("item_elc") String item_elc,
            @Field("desc_elc") String desc_elc,
            @Field("pdf_elc_image") String pdf_elc_image,
            @Field("pdf_elc") String pdf_elc
    );

    @FormUrlEncoded
    @POST("helpmecadd.php")
    Call<HelpDataMec> helpmecaddResponse(
            @Field("item_mec") String item_mec,
            @Field("desc_mec") String desc_mec,
            @Field("pdf_mec_image") String pdf_mec_image,
            @Field("pdf_mec") String pdf_mec
    );

    @FormUrlEncoded
    @POST("wmspopup.php")
    Call<WmsData> wmspopupData(
            @Field("wms_id") String wms_id
    );

    @FormUrlEncoded
    @POST("helpelcpdf.php")
    Call<WmsData> helpelcpdfData(
            @Field("help_elc_id") String help_elc_id
    );

    @FormUrlEncoded
    @POST("helpmecpdf.php")
    Call<WmsData> helpmecpdfData(
            @Field("help_mec_id") String help_mec_id
    );

    @FormUrlEncoded
    @POST("warehouseget.php")
    Call<WmsResponseData> aiGetData(
            @Field("wms_id") String wms_id
    );

    @FormUrlEncoded
    @POST("helpelcget.php")
    Call<HelpResponseDataElc> aiElcGetData(
            @Field("help_elc_id") String help_elc_id
    );

    @FormUrlEncoded
    @POST("helpmecget.php")
    Call<HelpResponseDataMec> aiMecGetData(
            @Field("help_mec_id") String help_mec_id
    );

    @FormUrlEncoded
    @POST("monitoringadd.php")
    Call<MonData> aiMonAddData(
            @Field("line") String line,
            @Field("station") String station,
            @Field("mon_image") String mon_image,
            @Field("mon_status") String mon_status,
            @Field("mon_pic") String mon_pic,
            @Field("mon_desc") String mon_desc
    );

    @FormUrlEncoded
    @POST("monitoringupdate.php")
    Call<MonData> aiMonUpdateData(
            @Field("mon_id") String mon_id,
            @Field("mon_desc") String mon_desc,
            @Field("mon_image") String mon_image
    );

    @FormUrlEncoded
    @POST("monitoringdelete.php")
    Call<MonData> mondeleteData(
            @Field("mon_id") String mon_id,
            @Field("line") String line,
            @Field("station") String station
    );

    @FormUrlEncoded
    @POST("monitoringview.php")
    Call<MonResponseData> monviewData(
            @Field("mon_id") String mon_id
    );

    @FormUrlEncoded
    @POST("assetstation.php")
    Call<AssetResponseData> aiAssetStationData(
            @Field("asset_line") String asset_line
    );

    @FormUrlEncoded
    @POST("assetmachine.php")
    Call<AssetResponseData> aiAssetMachineData(
            @Field("asset_line") String asset_line,
            @Field("asset_station") String asset_station
    );

    @FormUrlEncoded
    @POST("assetview.php")
    Call<AssetResponseData> aiAssetViewData(
            @Field("asset_line") String asset_line,
            @Field("asset_station") String asset_station,
            @Field("machine_name") String machine_name
    );

    @FormUrlEncoded
    @POST("assetadd.php")
    Call<AssetData> aiAssetAddData(
            @Field("asset_no") String asset_no,
            @Field("machine_name") String machine_name,
            @Field("asset_part") String asset_part,
            @Field("asset_category") String asset_category,
            @Field("asset_qty") String asset_qty,
            @Field("asset_line") String asset_line,
            @Field("asset_station") String asset_station,
            @Field("asset_unit") String asset_unit
    );

    @FormUrlEncoded
    @POST("assetaddmachine.php")
    Call<AssetData> aiAssetAddMachineData(
            @Field("asset_line") String asset_line,
            @Field("asset_station") String asset_station,
            @Field("machine_name") String machine_name
    );

    @FormUrlEncoded
    @POST("assetreplace.php")
    Call<AssetResponseData> aiAssetReplaceData(
            @Field("asset_id") String asset_id
    );

    @FormUrlEncoded
    @POST("assetwarehouse.php")
    Call<WmsResponseData> aiAssetWarehouseData(
            @Field("asset_part") String asset_part
    );

    @FormUrlEncoded
    @POST("assetcopro.php")
    Call<AssetData> aiAssetCoproData(
            @Field("asset_id") String asset_id,
            @Field("asset_part") String asset_part,
            @Field("copro") String copro,
            @Field("asset_qty") String asset_qty
    );

    @FormUrlEncoded
    @POST("requestitem.php")
    Call<HistoryData> aiRequestData(
            @Field("line_hist") String line_hist,
            @Field("station_hist") String station_hist,
            @Field("machine_hist") String machine_hist,
            @Field("pic_hist") String pic_hist,
            @Field("part_problem") String part_problem,
            @Field("qty_problem") String qty_problem,
            @Field("problem") String problem,
            @Field("image_problem") String image_problem
    );

    @FormUrlEncoded
    @POST("otherproblem.php")
    Call<HistoryData> aiProblemData(
            @Field("line_hist") String line_hist,
            @Field("station_hist") String station_hist,
            @Field("machine_hist") String machine_hist,
            @Field("pic_hist") String pic_hist,
            @Field("problem") String problem,
            @Field("title") String title,
            @Field("image_problem") String image_problem
    );

    @FormUrlEncoded
    @POST("solution.php")
    Call<HistoryData> aiSolutionData(
            @Field("id_hist") String id_hist,
            @Field("solution") String solution,
            @Field("title") String title,
            @Field("image_solution") String image_solution
    );

    @FormUrlEncoded
    @POST("problem.php")
    Call<HistoryResponseData> aiGetProblemData(
            @Field("id_hist") String id_hist
    );


    @GET("warehouse.php")
    Call<WmsResponseData> aiWarehouseData();

    @GET("warehousetag.php")
    Call<WmsData> aiWarehouseTag();

    @GET("helpelektrik.php")
    Call<HelpResponseDataElc> aiHelpElcData();

    @GET("helpmekanik.php")
    Call<HelpResponseDataMec> aiHelpMecData();

    @GET("monitoring1.php")
    Call<MonResponseData> aiMon1Data();

    @GET("monitoring2.php")
    Call<MonResponseData> aiMon2Data();

    @GET("monitoring3.php")
    Call<MonResponseData> aiMon3Data();

    @GET("monitoring4.php")
    Call<MonResponseData> aiMon4Data();

    @GET("assetline.php")
    Call<AssetResponseData> aiAssetData();

    @GET("assetgetnotifdate.php")
    Call<AssetResponseData> aiAssetNotifData();

    @GET("assetgetpart.php")
    Call<AssetResponseData> aiAssetPartData();

    @GET("history.php")
    Call<HistoryResponseData> aiHistoryData();

    @GET("technical.php")
    Call<HistoryResponseData> aiTechnicalData();

}