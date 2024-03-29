import { UserStatusODto } from "../../api/dto/ODto";
import { useConnRtsIdsTest, useConnRtsList } from "../../api/routes/ConnRtsHk";
import { useNodeRtsStatus } from "../../api/routes/NodeRtsHk";
import { useUserRtsStatus } from "../../api/routes/UserRtsHk";
import { Conn, Pipeline, Profil } from "../../assets";
import { Disclosure } from "@headlessui/react";
import { Link, useLocation } from "react-router-dom";

/**
 * Sidebar component.
 */
export default function SideBarCpt() {
  // Get current path
  const location = useLocation();
  const currentPath = location.pathname;

  // Render
  return (
    <div className="flex h-screen w-12 flex-col">
      <div className="flex grow flex-col justify-center">
        <IconTabLinkCpt to="connections" title="Connections" svg={Conn} isCurrent={currentPath == "/app/connections"} />
        <IconTabLinkCpt to="pipelines" title="Pipelines" svg={Pipeline} isCurrent={currentPath == "/app/pipelines"} />
      </div>
      <IconUserCpt />
    </div>
  );
}

/**
 * Tooltip component.
 */
function TooltipCpt(props: { text: string; className?: string }) {
  return (
    <div
      className={
        "pointer-events-none select-none rounded bg-neutral bg-opacity-80 px-1.5 py-1 text-xs font-thin text-neutral-content shadow" +
        "transition-all duration-300 ease-in-out" +
        (props.className ? ` ${props.className}` : "")
      }
    >
      {props.text}
    </div>
  );
}

/**
 * Sidebar icon tab component.
 */
function IconTabLinkCpt(props: {
  to: string;
  title: string;
  svg: React.FunctionComponent<
    React.SVGProps<SVGSVGElement> & {
      title?: string | undefined;
    }
  >;
  isCurrent: boolean;
}) {
  return (
    <div className="relative flex flex-col justify-center p-2.5">
      <span
        className={`absolute -ml-2.5 h-full w-0.5 bg-primary
        transition-all duration-300 ease-in-out ${props.isCurrent ? "scale-100" : "scale-0"}`}
      />
      <Link to={props.to} className="peer">
        <props.svg className={"fill-primary" + (props.isCurrent ? "" : " contrast-50 hover:contrast-100")} />
      </Link>
      <TooltipCpt text={props.title} className="absolute left-full ml-1 origin-left scale-0 peer-hover:scale-100" />
    </div>
  );
}

/**
 * Sidebar icon user component.
 */
function IconUserCpt() {
  // Hooks & Retrieve data
  const user = useUserRtsStatus();

  // Render
  return (
    <Disclosure>
      <div className="relative flex flex-col justify-end p-2.5">
        {/* Status icon */}
        <Disclosure.Button className="relative flex items-center">
          {({ open }) => (
            <>
              <Profil className={"peer fill-primary" + (open ? "" : " contrast-50 hover:contrast-100")} />
              <span
                className={
                  "absolute bottom-0 right-0 h-2.5 w-2.5 rounded-full border-2 border-base-100" +
                  (user ? " bg-success" : " bg-warning")
                }
              />
              {!open && (
                <TooltipCpt
                  text="Status"
                  className="absolute left-full ml-3.5 origin-left scale-0 peer-hover:scale-100"
                />
              )}
            </>
          )}
        </Disclosure.Button>

        {/* Status panel */}
        <Disclosure.Panel className="absolute left-full ml-1">
          <PanelStatusCpt user={user} />
        </Disclosure.Panel>
      </div>
    </Disclosure>
  );
}

/**
 * User status panel component.
 */
function PanelStatusCpt(props: { user: UserStatusODto | undefined }) {
  // Hooks & Retrieve data
  const conns = useConnRtsList();
  const isUps = useConnRtsIdsTest(conns?.map((_) => _.id));
  const nodeStatus = useNodeRtsStatus();

  // Process data
  const isUpStat = isUps && { up: isUps.filter((_) => _ === true).length, total: isUps.length };
  const cpuStat = nodeStatus && { usage: nodeStatus.cpu, total: nodeStatus.cpuTotal };
  const ramStat = nodeStatus && { usage: nodeStatus.ram, total: nodeStatus.ramTotal };

  // isUpStat: Text color logic
  let isUpTextColor: string;
  if (isUpStat === undefined) isUpTextColor = "text-warning";
  else if (isUpStat.up === isUpStat.total) isUpTextColor = "text-success";
  else isUpTextColor = "text-error";

  // cpuStat & ramStat: Text color logic
  const usageTextColor = (stat: { usage: number; total: number } | undefined) => {
    if (stat === undefined) return " text-warning";
    else {
      const percentage = stat.usage / stat.total;
      if (percentage > 0.9) return " text-error";
      if (percentage < 0.75) return " text-success";
      return " text-warning";
    }
  };

  // Render
  return (
    <div
      className="flex select-none flex-col space-y-2 rounded
      bg-neutral bg-opacity-80 fill-neutral-content text-xs font-thin text-neutral-content shadow
      transition-all duration-300 ease-in-out hover:bg-opacity-95"
    >
      {/* My Status */}
      <div className="flex flex-col items-center space-y-1 px-2 pt-2">
        <h1 className="text-sm font-semibold">User</h1>
        <div className="flex items-center space-x-4">
          <div className="flex flex-col">
            <p className="font-bold">{props.user ? props.user.username : "???"}</p>
            <p className="italic">#{props.user ? props.user.id : "?"}</p>
            {props.user ? <p className="text-success">(Connected)</p> : <p className="text-warning">(Connecting)</p>}
          </div>
          <div className="flex flex-col">
            <div className="flex items-center space-x-1.5">
              <Conn className="h-5" />
              <p className={isUpTextColor}>{isUpStat ? `${isUpStat.up}/${isUpStat.total}` : "?/?"}</p>
            </div>
            <div className="flex items-center space-x-1.5">
              <Pipeline className="h-5" />
              <p className="text-warning">?/?</p>
            </div>
          </div>
        </div>
      </div>

      {/* Nodes status */}
      <div className="flex flex-col items-center space-y-1 px-2 pb-2">
        <h1 className="text-sm font-semibold">Nodes</h1>
        <div className="flex space-x-2">
          <div className="flex flex-col items-center">
            <p>Node(s)</p>
            <p>{nodeStatus ? nodeStatus.nbNodes : "?"}</p>
          </div>
          <div className={"flex flex-col items-center" + usageTextColor(cpuStat)}>
            <p>CPU</p>
            <p>{cpuStat ? `${cpuStat.usage.toFixed(1)}/${cpuStat.total.toFixed(1)}` : "?/?"}</p>
          </div>
          <div className={"flex flex-col items-center" + usageTextColor(ramStat)}>
            <p>RAM</p>
            <p>{ramStat ? `${ramStat.usage.toFixed(1)}/${ramStat.total.toFixed(1)}` : "?/?"}</p>
          </div>
        </div>
      </div>
    </div>
  );
}
