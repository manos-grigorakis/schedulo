interface MainButtonProps {
  title: string;
}

export default function MainButton({ title }: MainButtonProps) {
  return (
    <button
      type="submit"
      className="w-full py-2 mt-6 text-center text-white rounded-lg cursor-pointer bg-primary-500 hover:bg-primary-600"
    >
      {title}
    </button>
  );
}
