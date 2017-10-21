import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadTransactionsComponent } from './upload-transactions.component';

describe('UploadTransactionsComponent', () => {
  let component: UploadTransactionsComponent;
  let fixture: ComponentFixture<UploadTransactionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UploadTransactionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UploadTransactionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
