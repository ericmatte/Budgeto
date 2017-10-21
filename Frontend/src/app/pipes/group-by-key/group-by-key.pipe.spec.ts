import { GroupByKeyPipe } from './group-by-key.pipe';
import { FilterPipe } from "../filter/filter.pipe";
let pipe: GroupByKeyPipe;

let cities = [{ cityId: 1, name: "London" }, { cityId: 2, name: "Manchester" }, { cityId: 3, name: "Amsterdam" }];
let areas = [
  { name: "Buckingham palace", cityId: 1 },
  { name: "Wembley", cityId: 1 },
  { name: "Media City", cityId: 2 },
  { name: "Old Trafford", cityId: 2 },
  { name: "Baltimore" }
];

beforeEach(() => pipe = new GroupByKeyPipe());
describe('GroupByKeyPipe', () => {
  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('should create an object of all cities by their id', () => {
    var expectingOutput = {1: cities[0], 2: cities[1], 3: cities[2]};

    var output = pipe.transform(cities, 'cityId')
    expect(output).toEqual(expectingOutput);
  });

  it('should create an object of areas by their city id, forgetting the duplicate', () => {
    var expectingOutput = {1: areas[1], 2: areas[3]};

    var output = pipe.transform(areas, 'cityId')
    expect(output).toEqual(expectingOutput);
  });
});
