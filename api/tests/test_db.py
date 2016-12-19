from models.user import User


def test_db(client):
    users = User.get_all()
    assert len(users) > 0

