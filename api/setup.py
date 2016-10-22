from setuptools import setup

setup(
    name='Flask API',
    version='1.0',
    long_description=__doc__,
    packages=['flask-api'],
    include_package_data=True,
    zip_safe=False,
    install_requires=[
        'Flask>=0.11.1',
        'Jinja2>=2.8',
        'MarkupSafe>=0.23',
        'Werkzeug>=0.11.10',
        'click>=6.6',
        'itsdangerous>=0.24',
        'pytest>=3.0.3',
        'rsa>=3.4.2'
    ]
)