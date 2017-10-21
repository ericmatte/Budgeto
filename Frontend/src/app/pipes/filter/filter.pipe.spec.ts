import { FilterPipe } from './filter.pipe';
let pipe: FilterPipe;

let cities = [{ cityId: 1, name: "London" }, { cityId: 2, name: "Manchester" }, { cityId: 3, name: "Amsterdam" }];
let areas = [
  { name: "Buckingham palace", cityId: 1 },
  { name: "Wembley", cityId: 1 },
  { name: "Media City", cityId: 2 },
  { name: "Old Trafford", cityId: 2 },
  { name: "Baltimore" }
];

beforeEach(() => pipe = new FilterPipe());
describe('FilterPipe', () => {
  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('should get city with id=1', () => {
    expect(pipe.transform(cities, {cityId:1})).toEqual([{ cityId: 1, name: "London" }]);
  });

  it('should get areas with cityId=2', () => {
    var expectingOutput = [{ name: "Media City", cityId: 2 }, { name: "Old Trafford", cityId: 2 }];
    expect(pipe.transform(areas, {cityId:2})).toEqual(expectingOutput);
  });
});
