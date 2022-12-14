import { ReactComponent as DataFramesSvg } from "../assets/dataframes.svg";
import { ReactComponent as RanksSvg } from "../assets/ranks.svg";
import { Link } from "react-router-dom";

// SideBar
export const SideBar = () => {
  // Render
  return (
    <div className="flex h-screen w-14">
      <div className="fixed flex h-screen w-14 flex-col items-center justify-center bg-gray-900 drop-shadow">
        <Icon svg={DataFramesSvg} description={"DataFrames"} href={"/dataframes"} />
        <Icon svg={RanksSvg} description={"Ranks"} href={"/ranks"} />
      </div>
    </div>
  );
};

// Icon in SideBar
interface IconProps {
  svg: React.FunctionComponent<
    React.SVGProps<SVGSVGElement> & {
      title?: string | undefined;
    }
  >;
  description: string;
  href: string;
}
const Icon = (props: IconProps) => {
  // Render
  return (
    <Link
      to={props.href}
      className="group m-2 flex h-11 w-11 items-center justify-center
      rounded-3xl fill-rose-500 p-2 drop-shadow transition-all duration-200 ease-linear
      hover:rounded-xl hover:bg-rose-500 hover:fill-white">
      {/* Icon */}
      <props.svg className="h-auto w-auto" />

      {/* Tips */}
      <span
        className="pointer-events-none absolute left-12 m-2 min-w-max origin-left scale-0 select-none
      rounded-xl bg-gray-900 p-2 text-xs font-bold text-white drop-shadow transition-all duration-200
      ease-linear group-hover:scale-100">
        {props.description}
      </span>
    </Link>
  );
};