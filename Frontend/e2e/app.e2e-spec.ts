import { BudgetoPage } from './app.po';

describe('budgeto App', () => {
  let page: BudgetoPage;

  beforeEach(() => {
    page = new BudgetoPage();
  });

  it('should display message saying My First Angular App', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('My First Angular App!');
  });
});
