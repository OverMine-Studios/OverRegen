package studio.overmine.overregion.models;

import lombok.Getter;
import okhttp3.*;
import org.json.JSONObject;
import studio.overmine.overregion.OverRegen;

@Getter
public class License {

    private LicenseStatus status;

    public License(OverRegen plugin, String license) {
        if (license == null || license.isEmpty()) {
            this.status = LicenseStatus.INVALID;
        }
        else {
            try {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{\n    \"license\": \""+ license +"\",\n    \"product\": \"OverRegen\",\n    \"version\": \""+ plugin.getDescription().getVersion() +"\"\n}");
                Request request = new Request.Builder()
                        .url("https://license.risas.me/api/client")
                        .method("POST", body)
                        .addHeader("Authorization", "42RDgHeygEg9pphK1Gxsj7VZEDURZEnF")
                        .build();

                Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();

                if (responseBody == null) {
                    this.status = LicenseStatus.ERROR;
                    return;
                }

                JSONObject obj = new JSONObject(responseBody.string());

                switch (obj.getString("status_overview")) {
                    case "success":
                        this.status = LicenseStatus.SUCCESS;
                        break;
                    case "failed":
                        this.status = LicenseStatus.INVALID;
                        break;
                    default:
                        this.status = LicenseStatus.ERROR;
                        break;
                }

                responseBody.close();
            }
            catch (Exception e) {
                this.status = LicenseStatus.ERROR;
                plugin.getLogger().severe("Failed to connect to the license server.");
            }
        }
    }

    public enum LicenseStatus {
        SUCCESS("&a"),
        INVALID("&c"),
        ERROR("&c");

        public final String color;

        LicenseStatus(String color) {
            this.color = color;
        }

        public String getColorName() {
            return color + name();
        }
    }
}
