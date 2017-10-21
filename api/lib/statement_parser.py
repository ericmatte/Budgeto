import csv
import os
import re
from datetime import datetime

from models import Bank


class StatementParser:


    def __init__(self, bank_name='', bank=None):
        self.bank = bank or Bank.get(name=bank_name.title())
        if self.bank is None:
            raise ValueError('Bank not supported.')

        # Defining each bank parsing method
        if self.bank.name == 'Tangerine':
            """
            Tangerine CSV format:
            Transaction date,Transaction,Name,Memo,Amount
            """
            # 0-Date, 2-Description, 4-Amount
            self.description = lambda row: row[2]
            self.amount = lambda row: float(row[4] or '0')
            self.date = lambda row: self.format_date(row[0], '%m/%d/%Y')
            self.start_at = 1
            self.columns = 5

        elif self.bank.name == 'Desjardins':
            """
            Desjardins CSV format:
            https://www.desjardins.com/en/services_en_ligne/accesd_affaires/soutien/guiderel.pdf
            """
            # 3-Date, 5-Description, 6-Cheque #, 7-Withdrawal, 8-Deposit
            self.description = lambda row: row[5] + (' #' + row[6] if row[6] else '')
            self.amount = lambda row: float(row[8] or '0') or -float(row[7] or '0')
            self.date = lambda row: self.format_date(row[3], '%Y/%m/%d')
            self.start_at = 0
            self.columns = 14

        else:
            raise ValueError('Bank not supported yet.')

    def parse(self, file):
        if not os.path.isfile(file):
            raise ValueError('Filetype \'%s\' not found.' % file)

        extension = os.path.splitext(file)[1].lower()
        if extension == '.csv':
            return self.__parse_csv(file)
        else:
            raise ValueError('Filetype \'%s\' not supported.' % extension)

    @staticmethod
    def clean_text(text):
        text = re.sub(r'[ \t]+', ' ', text) # Remove trailing spaces and tabs
        text = re.sub(r'( *)\/( *)', ' / ', text) # Put uniform spaces around '/'
        text = re.sub(r'([A-Z])([A-Z]+)\b', lambda m: m.group(1)+m.group(2).lower(), text) # Change uppercase words to title
        return text.strip()

    @staticmethod
    def format_date(date_str, format='%Y-%m-%d'):
        date_str = date_str or 'now'
        try:
            date = datetime.strptime(date_str, format).date()
        except ValueError:
            date = datetime.now().date()
        return date.isoformat()

    def __parse_csv(self, file):
        with open(file, newline='') as csv_file:
            reader = csv.reader(csv_file, delimiter=',')
            rows = [[value.strip() for value in row] for row in reader]

            output = {'bank':self.bank.name, 'transactions':[]}

            for i in range(self.start_at, len(rows)):
                row = rows[i]
                if len(row) == self.columns:
                    transaction = {
                        'description': self.clean_text(self.description(row)),
                        'amount': self.amount(row),
                        'date':  self.date(row)
                    }
                    output['transactions'].append(transaction)

            return output
