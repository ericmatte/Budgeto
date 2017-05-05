import codecs
import json
from datetime import datetime

from lib.print_colors import out
from lib.statement_parser import StatementParser

def test_csv_to_json(client, statements):
    for statement in statements:
        out.print('Test: ' + statement['csv'], out.WARNING)
        parsed = StatementParser(statement['bank']).parse(statement['csv'])
        with codecs.open(statement['json'], 'r', encoding='utf8') as f:
            ref = json.loads(f.read())

            assert parsed['bank'] == ref['bank']
            for i in range(len(parsed['transactions'])):
                assert parsed['transactions'][i]['description'] == ref['transactions'][i]['description']
                assert parsed['transactions'][i]['date'] == ref['transactions'][i]['date']
                assert parsed['transactions'][i]['amount'] == ref['transactions'][i]['amount']

    # Testing limit cases
    def try_that(func, error, *arg):
        try:
            data = func(*arg)
            return False
        except Exception as e:
            return error in str(e)

    assert try_that(StatementParser, 'Bank not supported', 'NotABank')
    assert try_that(StatementParser, 'Bank not supported', '')
    assert try_that(StatementParser('Tangerine').parse, 'not found', 'NotAFile')
    assert try_that(StatementParser('Tangerine').parse, 'not supported', statements[0]['json'])

def test_clean_text(client):
    strings = [
        {'dirty': 'TPS /CANADA',            'clean': 'Tps / Canada'},
        {'dirty': 'A TEST at Rand Inc.',    'clean': 'A Test at Rand Inc.'},
        {'dirty': 'A CamelCase name',       'clean': 'A CamelCase name'},
        {'dirty': '   To   much spaces   ', 'clean': 'To much spaces'}
    ]
    for string in strings:
        clean = StatementParser.clean_text(string['dirty'])
        assert clean == string['clean']

def test_format_date(client):
    now = datetime.now().date().isoformat()
    dates = [
        {'input': '4/9/2017',   'format': '%d/%m/%Y', 'iso': '2017-09-04', 'test': 'Tangerine'},
        {'input': '2017/03/31', 'format': '%Y/%m/%d', 'iso': '2017-03-31', 'test': 'Desjardins'},
        {'input': '4/26/2017',  'format': '%Y.%m.%d', 'iso': now,          'test': 'Wrong format'},
        {'input': '',           'format': '',         'iso': now,          'test': 'No date'}
    ]
    for date in dates:
        out.print('Test: ' + date['test'], out.WARNING)
        formatted = StatementParser.format_date(date['input'], date['format'])
        assert formatted == date['iso']


