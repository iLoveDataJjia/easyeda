import { motion } from "framer-motion";

// Profil
export const Profil = () => (
  <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }} className="m-auto">
    Profil in UX design...
  </motion.div>
);
