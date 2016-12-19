from sqlalchemy import create_engine
from sqlalchemy.orm import scoped_session, sessionmaker
from sqlalchemy.ext.declarative import declarative_base
from sshtunnel import SSHTunnelForwarder


_db_session = None
DeclarativeBase = declarative_base()


def get_session():
    if _db_session is None:
        raise RuntimeError('You must call init_db(db_connection_string: str) first.')
    return _db_session


def init_db(db_connection_string, config):
    global _db_session
    if config.get('USE_TUNNEL'):
        server = SSHTunnelForwarder(
            ('endlessapi.ddns.net', 22),
            ssh_username=config['SERVER_USER'],
            ssh_password=config['SERVER_PWD'],
            remote_bind_address=('localhost', 3306),
            local_bind_address=('localhost', config['SERVER_TUNNEL_PORT']))
        server.start()
        print('Tunneling {0} on port {1}'.format(server.ssh_host, str(server.local_bind_port)))
    # import all modules here that might define models so that
    # they will be registered properly on the metadata.  Otherwise
    # you will have to import them first before calling init_db()
    engine = create_engine(db_connection_string, convert_unicode=True)
    _db_session = scoped_session(sessionmaker(autocommit=False, autoflush=False, bind=engine))
    DeclarativeBase.session = _db_session
    DeclarativeBase.query = _db_session.query_property()
    DeclarativeBase.metadata.create_all(bind=engine)