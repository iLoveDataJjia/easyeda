import imgProfilPng from "../assets/_old/imgProfil.png";
import { ReactComponent as DownloadIconSvg } from "../assets/downloadIcon.svg";
import { ReactComponent as StarIconSvg } from "../assets/starIcon.svg";
import { ReactComponent as LinkedInSvg } from "../assets/medias/linkedin.svg";
import { ReactComponent as GithubSvg } from "../assets/medias/github.svg";
import { ReactComponent as KaggleSvg } from "../assets/medias/kaggle.svg";
import { Link } from "react-router-dom";

// Profil information
export const ProfilInfo = () => {
  // Retrieve profil data
  const profil = {
    id: "3769",
    name: "Elorine",
    img: <img src={imgProfilPng} alt="" className="h-24 w-24 rounded-l-xl" />,
    downloadCount: 7366,
    startCount: 211,
    linkedinHref: "https://www.linkedin.com/in/james-jiang-87306b155/",
    githubHref: "https://github.com/iLoveDataJjia",
    kaggleHref: "https://www.kaggle.com/ilovedatajjia",
  };

  // Formatter
  const numberFormatter = Intl.NumberFormat("en-US", {
    notation: "compact",
    maximumFractionDigits: 1,
  }).format;

  // Render
  return (
    <div className="flex h-24 w-72 select-none items-center rounded-xl bg-gray-900 drop-shadow-md">
      {/* Photo */}
      <Link to="/profil" className="h-24">
        {profil.img}
      </Link>

      {/* Infos */}
      <div className="flex flex-1 justify-evenly">
        {/* Profil info */}
        <div className="flex flex-col">
          {/* Identifier & Name */}
          <div className="group/idname flex flex-col">
            <div className="flex">
              <Link
                to="/profil"
                className="text-xs text-green-500 duration-200 ease-linear
               group-hover/idname:text-white">{`#${profil.id}`}</Link>
            </div>
            <div className="flex">
              <Link
                to="/profil"
                className="text-2xl font-bold text-green-500 transition-all duration-200 ease-linear
               group-hover/idname:text-white">
                {profil.name}
              </Link>
            </div>
          </div>

          {/* Downloads & Stars */}
          <div className="flex space-x-3">
            <Link to="/profil" className="group/downloads flex items-center space-x-1">
              <DownloadIconSvg
                className="h-4 w-4 fill-green-500 transition-all duration-200 ease-linear
               group-hover/downloads:fill-white"
              />
              <div
                className="text-xs text-green-500 transition-all duration-200 ease-linear
               group-hover/downloads:text-white">
                {numberFormatter(profil.downloadCount)}
              </div>
            </Link>
            <Link to="/profil" className="group/stars flex items-center space-x-1">
              <StarIconSvg
                className="h-4 w-4 fill-green-500 transition-all duration-200 ease-linear
               group-hover/stars:fill-white"
              />
              <div
                className="text-xs text-green-500 transition-all duration-200 ease-linear
               group-hover/stars:text-white">
                {numberFormatter(profil.startCount)}
              </div>
            </Link>
          </div>
        </div>

        {/* Social media info */}
        <div className="flex flex-col items-center justify-between">
          <a href={profil.linkedinHref} target="_blank" rel="noreferrer">
            <LinkedInSvg
              className="h-4 w-4 fill-green-500 transition-all
              duration-200 ease-linear hover:fill-white"
            />
          </a>
          <a href={profil.githubHref} target="_blank" rel="noreferrer">
            <GithubSvg
              className="h-4 w-4 fill-green-500 transition-all
              duration-200 ease-linear hover:fill-white"
            />
          </a>
          <a href={profil.kaggleHref} target="_blank" rel="noreferrer">
            <KaggleSvg
              className="h-4 w-4 fill-green-500 transition-all
              duration-200 ease-linear hover:fill-white"
            />
          </a>
        </div>
      </div>
    </div>
  );
};
