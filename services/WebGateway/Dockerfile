FROM python:3.10-alpine

RUN pip install poetry
WORKDIR /app

COPY poetry.lock pyproject.toml ./

RUN poetry install --no-dev --no-root

COPY main.py ./

EXPOSE 7001

CMD ["poetry", "run", "uvicorn", "main:app", "--host", "0.0.0.0", "--port", "7001"]

# docker build -t devminty/ita-web-gateway:1.2 .
# docker run -it -d -p 7001:7001 --name ITA-WEB-GATEWAY devminty/ita-web-gateway:1.2
# docker push devminty/ita-web-gateway:1.2