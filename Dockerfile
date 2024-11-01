FROM ubuntu:latest
LABEL authors="MSI"

ENTRYPOINT ["top", "-b"]