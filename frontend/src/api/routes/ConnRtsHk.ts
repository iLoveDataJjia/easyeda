import { ToastLevelEnum } from "../../context/toaster/ToasterCtx";
import useToaster from "../../context/toaster/ToasterHk";
import { ConnFormIDto } from "../dto/IDto";
import { ConnStatusODto, ConnTestODto } from "../dto/ODto";
import { useGet, usePostM } from "./GenericRtsHk";
import { useEffect } from "react";

/**
 * Connection testing hook for route ("/conn/test").
 */
export function useConnRtsTest() {
  const { addToast } = useToaster();
  const { postM, data, isLoading } = usePostM<ConnTestODto>("/conn/test", true, true);
  useEffect(
    () =>
      data
        ? addToast({
            level: ToastLevelEnum.Success,
            header: "Connection is UP.",
          })
        : addToast({
            level: ToastLevelEnum.Warning,
            header: "Connection is DOWN.",
          }),
    [isLoading]
  );
  return {
    test: (body: ConnFormIDto) => postM(body, undefined),
    isTesting: isLoading,
  };
}

/**
 * Connection listing hook for route ("/conn/list").
 */
export function useConnRtsList() {
  const { data, isLoading } = useGet<ConnStatusODto[]>("/conn/list", undefined, true, false, 10);
  return { connsStatus: data, isLoading };
}

/**
 * Test known connection ("/conn/{id}/test").
 */
export function useConnRtsIdTest(connId: number) {
  const { data, isLoading } = useGet<ConnTestODto>(`/conn/${connId}/test`, undefined, true, false, 10);
  return { connTest: data, isLoading };
}
