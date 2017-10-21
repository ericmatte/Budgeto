import { GroupByPipe } from './group-by.pipe';
import { FilterPipe } from "../filter/filter.pipe";
let pipe: GroupByPipe;

let cities = [{ cityId: 1, name: "London" }, { cityId: 2, name: "Manchester" }, { cityId: 3, name: "Amsterdam" }];
let areas = [
  { name: "Buckingham palace", cityId: 1 },
  { name: "Wembley", cityId: 1 },
  { name: "Media City", cityId: 2 },
  { name: "Old Trafford", cityId: 2 },
  { name: "Baltimore" }
];

beforeEach(() => pipe = new GroupByPipe(new FilterPipe));
describe('GroupByPipe', () => {
  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('should group areas by cities', () => {
    var expectingOutput = [
      {
        cityId: 1,
        name: "London",
        items: [
          { name: "Buckingham palace", cityId: 1 },
          { name: "Wembley", cityId: 1 }
        ]
      },
      {
        cityId: 2,
        name: "Manchester",
        items: [
          { name: "Media City", cityId: 2 },
          { name: "Old Trafford", cityId: 2 }
        ]
      }
    ];

    var output = pipe.transform(areas, cities, 'cityId');
    expect(output).toEqual(expectingOutput);
  });

  it('should group areas by cities and keep areas with no city', () => {
    var expectingOutput = [
      {
        cityId: 1,
        name: "London",
        items: [
          { name: "Buckingham palace", cityId: 1 },
          { name: "Wembley", cityId: 1 }
        ]
      },
      {
        cityId: 2,
        name: "Manchester",
        items: [
          { name: "Media City", cityId: 2 },
          { name: "Old Trafford", cityId: 2 }
        ]
      },
      { cityId: 3, name: "Amsterdam", items: [] }
    ];

    var output = pipe.transform(areas, cities, 'cityId', 'cityId', true);
    expect(output).toEqual(expectingOutput);
  });
});
