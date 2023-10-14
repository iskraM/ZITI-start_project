FROM python:3.10-alpine

RUN pip install poetry
WORKDIR /app

COPY poetry.lock pyproject.toml ./

RUN poetry install --no-dev --no-root

COPY main.py ./

EXPOSE 7002

CMD ["poetry", "run", "uvicorn", "main:app", "--host", "0.0.0.0", "--port", "7002"]

# docker build -t devminty/ita-mobile-gateway:1.0 .
# docker run -it -d -p 7002:7002 --name ITA-MOBILE-GATEWAY devminty/ita-mobile-gateway:1.0
# docker push devminty/ita-mobile-gateway:1.0