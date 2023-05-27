import { toast } from "react-toastify";
import { ToastManagerLevel } from "./ToastManagerLevel";

export class ToastManager {
  sendMessage(message: string, title?: string, level?: ToastManagerLevel) {
    let toastMessage: JSX.Element = (
      <div>
        {title && <h4>{title}</h4>}
        <p>{message}</p>
      </div>
    );

    switch (level) {
      case ToastManagerLevel.SUCCESS:
        toast.success(toastMessage, {
          position: toast.POSITION.BOTTOM_CENTER,
        });
        return;

      case ToastManagerLevel.WARN:
        toast.warn(toastMessage, {
          position: toast.POSITION.BOTTOM_CENTER,
        });
        return;

      case ToastManagerLevel.ERROR:
        toast.error(toastMessage, {
          position: toast.POSITION.BOTTOM_CENTER,
        });
        break;

      default:
        toast(toastMessage, {
          position: toast.POSITION.BOTTOM_CENTER,
        });
        break;
    }
  }
}
