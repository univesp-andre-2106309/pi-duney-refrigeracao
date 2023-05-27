declare module "*.module.css";
declare module "*.module.scss";
declare module "*.svg" {
  const content: any;
  export default content;
}
// and so on for whatever flavor of css you're using