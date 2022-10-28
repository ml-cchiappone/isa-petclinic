import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HumanDetailComponent } from './human-detail.component';

describe('Human Management Detail Component', () => {
  let comp: HumanDetailComponent;
  let fixture: ComponentFixture<HumanDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HumanDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ human: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(HumanDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(HumanDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load human on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.human).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
