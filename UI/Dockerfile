FROM node:10 as react-build
WORKDIR /app
COPY . ./
RUN yarn
RUN yarn build

EXPOSE 3000
CMD ["yarn", "cross-env", "NODE_ENV=production", "run-p", "start", "server"]