import { UploadItem }    from 'angular2-http-file-upload';
import { ApiService } from "app/services/api.service";

export class MyUploadItem extends UploadItem {
    constructor(file: any, private apiService: ApiService) {
        super();
        this.url = apiService.api + 'upload-transactions'
        this.headers = apiService.getAuthorizationBearer();
        this.file = file;
    }
}