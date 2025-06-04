import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Injectable({ providedIn: 'root' })
export class I18nService {
  constructor(private translate: TranslateService) {
    translate.addLangs(['ua', 'en']);
    translate.setDefaultLang('ua');

    const browserLang = translate.getBrowserLang();
    translate.use(browserLang?.match(/ua|en/) ? browserLang : 'ua');
  }

  switchLang(lang: string) {
    this.translate.use(lang);
  }
}
