import { WithNullKeyPipe } from './with-null-key.pipe';
let pipe: WithNullKeyPipe;

let areas = [
  { name: "Buckingham palace", cityId: 1, world: 1 },
  { name: "Wembley", cityId: 1, world: 1 },
  { name: "Media City", cityId: 2, world: 1 },
  { name: "Old Trafford", world: 1},
  { name: "Baltimore", world: 1 }
];

beforeEach(() => pipe = new WithNullKeyPipe());
describe('WithNullKeyPipe', () => {
  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('should get areas with no cityId', () => {
    expect(pipe.transform(areas, 'cityId')).toEqual([{name: "Old Trafford", world: 1}, {name: "Baltimore", world: 1}]);
  });

  it('should get all areas', () => {
    expect(pipe.transform(areas, 'areaId')).toEqual(areas);
  });

  it('should get no areas', () => {
    expect(pipe.transform(areas, 'world')).toEqual([]);
  });
});
